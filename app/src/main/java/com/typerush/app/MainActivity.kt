package com.typerush.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

private val Ink = Color(0xFF0C1018)
private val Panel = Color(0xFF151C28)
private val Muted = Color(0xFF8D9AAF)
private val Lime = Color(0xFFC8F36A)
private val Blue = Color(0xFF79A9FF)
private val Passages = listOf(
    "The fastest way to learn is to stay curious, make small mistakes, and keep moving forward. Every word you type is another chance to improve.",
    "A quiet morning can bring a fresh start. Take a deep breath, focus on the next step, and let steady progress become your best habit.",
    "Great ideas often begin with simple questions. Explore them patiently, test what you learn, and turn your knowledge into something useful.",
    "Technology changes quickly, but clear thinking and good communication remain valuable skills. Practice a little each day and confidence will follow."
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TypeRushApp() }
    }
}

@Composable
fun TypeRushApp() {
    var screen by remember { mutableStateOf("home") }
    var duration by remember { mutableIntStateOf(60) }
    var passage by remember { mutableStateOf(Passages.random()) }
    var typed by remember { mutableStateOf("") }
    var remaining by remember { mutableIntStateOf(60) }
    var running by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }
    var history by remember { mutableStateOf(listOf<Pair<Int, Int>>()) }
    var roomCode by remember { mutableStateOf("") }
    var playerName by remember { mutableStateOf("Player") }
    var raceStatus by remember { mutableStateOf("Create a room or join a friend's room code.") }
    val correct = typed.indices.count { it < passage.length && typed[it] == passage[it] }
    val errors = typed.length - correct
    val accuracy = if (typed.isEmpty()) 100 else (correct * 100f / typed.length).roundToInt()
    val elapsed = duration - remaining
    val wpm = if (elapsed <= 0) 0 else ((correct / 5f) / (elapsed / 60f)).roundToInt()

    LaunchedEffect(running, remaining) {
        if (running && remaining > 0) {
            delay(1000)
            remaining--
        } else if (running && remaining == 0) {
            running = false
            finished = true
            history = listOf(wpm to accuracy) + history
        }
    }

    MaterialTheme(colorScheme = darkColorScheme(background = Ink, surface = Panel, primary = Lime, onPrimary = Ink, onBackground = Color.White, onSurface = Color.White)) {
        Column(Modifier.fillMaxSize().background(Ink).verticalScroll(rememberScrollState()).padding(horizontal = 22.dp, vertical = 24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("T", color = Ink, fontSize = 22.sp, fontWeight = FontWeight.Black, modifier = Modifier.background(Lime, RoundedCornerShape(10.dp)).padding(horizontal = 12.dp, vertical = 7.dp))
                Spacer(Modifier.width(10.dp))
                Text("TYPERUSH", color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp)
                Spacer(Modifier.weight(1f))
                Text("BETA", color = Lime, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(30.dp))
            when (screen) {
                "home" -> {
                    Text("TYPE FASTER.", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Black)
                    Text("THINK CLEARER.", color = Lime, fontSize = 34.sp, fontWeight = FontWeight.Black)
                    Spacer(Modifier.height(10.dp))
                    Text("A little practice goes a long way.", color = Muted, fontSize = 15.sp)
                    Spacer(Modifier.height(28.dp))
                    Text("PICK YOUR TIME", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(15,30,60,120).forEach { secs ->
                            Box(Modifier.weight(1f).background(if (duration == secs) Lime else Panel, RoundedCornerShape(12.dp)).clickable { duration = secs }.padding(vertical = 14.dp), contentAlignment = Alignment.Center) {
                                Text("${secs}s", color = if (duration == secs) Ink else Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(Modifier.height(18.dp))
                    ActionButton("START SOLO TEST") {
                        passage = Passages.random(); typed = ""; remaining = duration; running = false; finished = false; screen = "test"
                    }
                    Spacer(Modifier.height(12.dp))
                    ActionButton("MULTIPLAYER RACE", false) { screen = "multiplayer" }
                    Spacer(Modifier.height(26.dp))
                    Text("YOUR RECENT RESULTS", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                    Spacer(Modifier.height(10.dp))
                    if (history.isEmpty()) Text("Your completed tests will show up here.", color = Muted, fontSize = 13.sp)
                    history.take(5).forEachIndexed { i, result ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Test ${history.size - i}", color = Color.White)
                            Text("${result.first} WPM  ·  ${result.second}% accuracy", color = Lime, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                "test" -> {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("SOLO TEST", color = Muted, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Text(if (finished) "DONE" else "${remaining}s", color = Lime, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(18.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Stat("WPM", wpm.toString(), Modifier.weight(1f))
                        Stat("ACCURACY", "$accuracy%", Modifier.weight(1f))
                        Stat("ERRORS", errors.toString(), Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(22.dp))
                    Text(buildAnnotatedString {
                        passage.forEachIndexed { i, c ->
                            val color = when { i < typed.length && typed[i] == c -> Lime; i < typed.length -> Color(0xFFFF7777); i == typed.length -> Blue; else -> Muted }
                            withStyle(SpanStyle(color = color)) { append(c) }
                        }
                    }, fontSize = 19.sp, lineHeight = 31.sp)
                    Spacer(Modifier.height(18.dp))
                    BasicTextField(value = typed, onValueChange = { value ->
                        if (!finished && value.length <= passage.length) {
                            if (!running && value.isNotEmpty()) running = true
                            typed = value
                            if (value.length == passage.length) {
                                running = false; finished = true
                                val score = if (elapsed <= 0) 0 else ((correct / 5f) / (elapsed / 60f)).roundToInt()
                                history = listOf(score to accuracy) + history
                            }
                        }
                    }, enabled = !finished, textStyle = androidx.compose.ui.text.TextStyle(color = Color.White, fontSize = 16.sp), modifier = Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(12.dp)).padding(16.dp), decorationBox = { inner ->
                        Box { if (typed.isEmpty()) Text("Start typing here...", color = Muted); inner() }
                    })
                    Spacer(Modifier.height(22.dp))
                    if (finished) {
                        Text("TEST COMPLETE", color = Lime, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                        Text("Final score: $wpm WPM · $accuracy% accuracy", color = Color.White)
                        Spacer(Modifier.height(12.dp))
                        ActionButton("TRY AGAIN") { passage = Passages.random(); typed = ""; remaining = duration; finished = false; running = false }
                        Spacer(Modifier.height(8.dp))
                    }
                    ActionButton("BACK TO HOME", false) { running = false; screen = "home" }
                }
                "multiplayer" -> {
                    Text("RACE YOUR FRIENDS", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                    Text("Create a private room or enter a code to join.", color = Muted)
                    Spacer(Modifier.height(20.dp))
                    Text("YOUR DISPLAY NAME", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(playerName, { playerName = it.take(18) }, modifier = Modifier.fillMaxWidth(), singleLine = true, label = { Text("Name") })
                    Spacer(Modifier.height(14.dp))
                    ActionButton("CREATE PRIVATE ROOM") {
                        roomCode = (100000..999999).random().toString()
                        raceStatus = "Room code created. Live multiplayer is not connected yet."
                        screen = "room"
                    }
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(roomCode, { roomCode = it.filter(Char::isDigit).take(6) }, modifier = Modifier.fillMaxWidth(), singleLine = true, label = { Text("Room code") })
                    Spacer(Modifier.height(10.dp))
                    ActionButton("JOIN ROOM", false) {
                        raceStatus = if (roomCode.length == 6) "Room joining needs the multiplayer server connection." else "Enter a 6-digit room code."
                        screen = "room"
                    }
                    Spacer(Modifier.height(18.dp))
                    Text("Solo tests work offline. Online races need a configured multiplayer backend.", color = Muted, fontSize = 13.sp)
                    Spacer(Modifier.height(16.dp))
                    ActionButton("BACK", false) { screen = "home" }
                }
                "room" -> {
                    Text("PRIVATE ROOM", color = Muted, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Spacer(Modifier.height(14.dp))
                    Text(roomCode, color = Lime, fontSize = 42.sp, fontWeight = FontWeight.Black, letterSpacing = 4.sp)
                    Text("Share this code with your friend.", color = Muted)
                    Spacer(Modifier.height(20.dp))
                    Text(raceStatus, color = Color.White)
                    Spacer(Modifier.height(20.dp))
                    ActionButton("BACK TO MULTIPLAYER", false) { screen = "multiplayer" }
                }
            }
            Spacer(Modifier.height(30.dp))
            Text("TYPERUSH  ·  FIND YOUR FLOW", color = Color(0xFF536176), fontSize = 10.sp, letterSpacing = 1.5.sp)
        }
    }
}

@Composable
private fun ActionButton(label: String, primary: Boolean = true, onClick: () -> Unit) {
    Box(Modifier.fillMaxWidth().height(54.dp).background(if (primary) Lime else Panel, RoundedCornerShape(14.dp)).clickable(onClick = onClick).padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
        Text(label, color = if (primary) Ink else Color.White, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
    }
}

@Composable
private fun Stat(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier.background(Panel, RoundedCornerShape(12.dp)).padding(12.dp)) {
        Text(label, color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Text(value, color = Lime, fontSize = 21.sp, fontWeight = FontWeight.ExtraBold)
    }
}
