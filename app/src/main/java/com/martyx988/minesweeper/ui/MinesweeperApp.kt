package com.martyx988.minesweeper.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.martyx988.minesweeper.domain.AppScaffoldConfig
import com.martyx988.minesweeper.domain.AchievementId
import com.martyx988.minesweeper.domain.AppThemeChoice
import com.martyx988.minesweeper.domain.CellHazard
import com.martyx988.minesweeper.domain.CellVisibility
import com.martyx988.minesweeper.domain.ClassicBoardPresets
import com.martyx988.minesweeper.domain.Coordinate
import com.martyx988.minesweeper.domain.GameCellState
import com.martyx988.minesweeper.domain.GameMode
import com.martyx988.minesweeper.domain.MatchStatus
import com.martyx988.minesweeper.data.SharedPreferencesPlayerProfileStore
import com.martyx988.minesweeper.data.SharedPreferencesSessionSnapshotStore
import com.martyx988.minesweeper.ui.theme.MinesweeperTheme
import kotlin.random.Random

@Composable
internal fun MinesweeperApp() {
    val context = LocalContext.current
    val controller = remember {
        ResumeGameController(
            storage = SharedPreferencesSessionSnapshotStore(context),
            profileStore = SharedPreferencesPlayerProfileStore(context),
            initialConfig = ClassicBoardPresets.easy(seed = Random.nextLong()),
            nextSeed = { Random.nextLong() },
        )
    }

    MinesweeperTheme(themeChoice = controller.playerProfile.settings.themeChoice) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .statusBarsPadding()
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    HeaderCard(controller = controller)
                    StatusPanel(controller = controller)
                    GameBoardCard(controller = controller)
                    ProfilePanel(controller = controller)
                    FooterPanel(controller = controller)
                }
            }
        }

        if (controller.pendingResumeSnapshot != null) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Resume saved game?") },
                text = {
                    Text("An unfinished match was found on this device. Resume it or discard it and keep the new board.")
                },
                confirmButton = {
                    Button(onClick = { controller.confirmResume() }) {
                        Text("Resume")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { controller.dismissResume() }) {
                        Text("Discard")
                    }
                },
            )
        }

        controller.resumeNotice?.let { notice ->
            AlertDialog(
                onDismissRequest = controller::dismissResumeNotice,
                title = { Text("Saved game cleared") },
                text = { Text(notice) },
                confirmButton = {
                    Button(onClick = controller::dismissResumeNotice) {
                        Text("OK")
                    }
                },
            )
        }
    }
}

@Composable
private fun ProfilePanel(controller: ResumeGameController) {
    val profile = controller.playerProfile
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Wins ${profile.stats.wins}  |  Losses ${profile.stats.losses}  |  Flags ${profile.stats.flagsPlaced}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                profile.achievements.sortedBy { it.name }.forEach { achievement ->
                    AchievementBadge(label = achievementLabel(achievement))
                }
                if (profile.achievements.isEmpty()) {
                    Text(
                        text = "No achievements yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Button(
                onClick = controller::cycleTheme,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Theme: ${profile.playerThemeLabel()}")
            }
            OutlinedButton(
                onClick = { controller.setHapticsEnabled(!profile.settings.hapticsEnabled) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    if (profile.settings.hapticsEnabled) {
                        "Haptics: On"
                    } else {
                        "Haptics: Off"
                    },
                )
            }
        }
    }
}

@Composable
private fun HeaderCard(controller: ResumeGameController) {
    val currentMode = controller.gameState.board.config.mode
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(28.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = AppScaffoldConfig.appTitle,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag("app_title"),
            )
            Text(
                text = if (currentMode == GameMode.TRAP_TILES) {
                    "Trap Tiles adds 4 extra trap hazards to the field. Any revealed hazard ends the run."
                } else {
                    "Classic Easy is live now. Long-press cells to place flags or switch into Trap Tiles for a harsher board."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ModeToggle(
                    label = "Classic Easy",
                    active = currentMode == GameMode.CLASSIC_EASY,
                    onClick = { controller.switchMode(GameMode.CLASSIC_EASY) },
                )
                ModeToggle(
                    label = "Trap Tiles",
                    active = currentMode == GameMode.TRAP_TILES,
                    onClick = { controller.switchMode(GameMode.TRAP_TILES) },
                )
            }
        }
    }
}

@Composable
private fun ModeToggle(
    label: String,
    active: Boolean,
    onClick: () -> Unit,
) {
    if (active) {
        Button(onClick = onClick) {
            Text(text = label)
        }
    } else {
        OutlinedButton(onClick = onClick) {
            Text(text = label)
        }
    }
}

@Composable
private fun AchievementBadge(label: String) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(999.dp),
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun StatusPanel(controller: ResumeGameController) {
    val gameState = controller.gameState
    val flaggedCount = gameState.allCellStates.count { it.visibility == CellVisibility.FLAGGED }
    val remainingMines = gameState.board.config.mineCount + gameState.board.config.trapCount - flaggedCount
    val statusTitle = when (gameState.status) {
        MatchStatus.ACTIVE -> "Field active"
        MatchStatus.WON -> "Field cleared"
        MatchStatus.LOST -> "Mine triggered"
    }
    val statusBody = when (gameState.status) {
        MatchStatus.ACTIVE -> "Tap to reveal, long-press to flag."
        MatchStatus.WON -> "You cleared every safe tile."
        MatchStatus.LOST -> "All mines are shown. Restart for a new board."
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StatCard(
            modifier = Modifier.fillMaxWidth(),
            label = "Status",
            value = statusTitle,
            supporting = statusBody,
        )
    }
    Spacer(modifier = Modifier.height(12.dp))
    StatCard(
        modifier = Modifier.fillMaxWidth(),
        label = "Mines Left",
        value = remainingMines.toString(),
        supporting = "Flags: $flaggedCount",
    )
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    supporting: String,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = supporting,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun GameBoardCard(controller: ResumeGameController) {
    val gameState = controller.gameState
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(28.dp),
    ) {
        BoxWithConstraints {
            val cellSpacing = 8.dp
            val columns = gameState.board.columns
            val totalSpacing = cellSpacing * (columns - 1)
            val cellSize = (maxWidth - totalSpacing) / columns

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = "Board",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                gameState.board.cells.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(cellSpacing),
                    ) {
                        row.forEach { cell ->
                            val cellState = gameState.cellStateAt(cell.coordinate)
                            BoardTile(
                                state = cellState,
                                onReveal = { controller.reveal(cell.coordinate) },
                                onToggleFlag = { controller.toggleFlag(cell.coordinate) },
                                modifier = Modifier.size(cellSize),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BoardTile(
    state: GameCellState,
    onReveal: () -> Unit,
    onToggleFlag: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background = when (state.visibility) {
        CellVisibility.HIDDEN -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.16f)
        CellVisibility.FLAGGED -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.28f)
        CellVisibility.REVEALED -> MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = when {
        state.isDetonated -> MaterialTheme.colorScheme.error
        state.visibility == CellVisibility.FLAGGED -> MaterialTheme.colorScheme.secondary
        state.cell.hazard == CellHazard.MINE -> MaterialTheme.colorScheme.primary
        state.cell.adjacentMineCount > 0 -> numberColor(state.cell.adjacentMineCount)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .semantics {
                contentDescription = tileDescription(state)
            }
            .testTag("tile_${state.cell.coordinate.row}_${state.cell.coordinate.column}")
            .combinedClickable(
                onClick = onReveal,
                onLongClick = onToggleFlag,
            )
            .background(
                color = background,
                shape = RoundedCornerShape(14.dp),
            )
            .height(40.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = tileLabel(state),
            color = contentColor,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun FooterPanel(controller: ResumeGameController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Button(
                onClick = { controller.restart() },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("New Board")
            }
            OutlinedButton(
                onClick = { controller.restartWithCurrentSeed() },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Replay Seed")
            }
        }
    }

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = "Seed ${controller.gameState.board.config.seed}  |  Long-press to place a flag",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

private fun tileLabel(state: GameCellState): String = when (state.visibility) {
    CellVisibility.HIDDEN -> ""
    CellVisibility.FLAGGED -> "F"
    CellVisibility.REVEALED -> when {
        state.cell.hazard == CellHazard.MINE -> "X"
        state.cell.hazard == CellHazard.TRAP -> "!"
        state.cell.adjacentMineCount == 0 -> ""
        else -> state.cell.adjacentMineCount.toString()
    }
}

private fun tileDescription(state: GameCellState): String {
    val coordinate = "row ${state.cell.coordinate.row + 1}, column ${state.cell.coordinate.column + 1}"
    return when (state.visibility) {
        CellVisibility.HIDDEN -> "Hidden tile at $coordinate"
        CellVisibility.FLAGGED -> "Flagged tile at $coordinate"
        CellVisibility.REVEALED -> when {
            state.cell.hazard == CellHazard.MINE && state.isDetonated -> "Detonated mine at $coordinate"
            state.cell.hazard == CellHazard.MINE -> "Mine at $coordinate"
            state.cell.hazard == CellHazard.TRAP && state.isDetonated -> "Triggered trap tile at $coordinate"
            state.cell.hazard == CellHazard.TRAP -> "Trap tile at $coordinate"
            state.cell.adjacentMineCount == 0 -> "Empty revealed tile at $coordinate"
            else -> "Revealed tile at $coordinate with ${state.cell.adjacentMineCount} nearby mines"
        }
    }
}

@Composable
private fun numberColor(number: Int) = when (number) {
    1 -> MaterialTheme.colorScheme.primary
    2 -> MaterialTheme.colorScheme.secondary
    3 -> MaterialTheme.colorScheme.tertiary
    else -> MaterialTheme.colorScheme.onSurface
}

private fun achievementLabel(achievement: AchievementId): String = when (achievement) {
    AchievementId.FIRST_WIN -> "First Win"
    AchievementId.FIRST_LOSS -> "First Loss"
    AchievementId.FIRST_FLAG -> "First Flag"
}

private fun com.martyx988.minesweeper.domain.PlayerProfile.playerThemeLabel(): String = when (settings.themeChoice) {
    AppThemeChoice.CLASSIC -> "Classic"
    AppThemeChoice.FOREST -> "Forest"
    AppThemeChoice.SUNSET -> "Sunset"
}

@Preview(showBackground = true)
@Composable
private fun MinesweeperAppPreview() {
    MinesweeperApp()
}
