package hu.nilebuilder.game

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

private val Sand = Color(0xFFF4E2B8)
private val Papyrus = Color(0xFFFFF5DE)
private val NileBlue = Color(0xFF278FA6)
private val DarkBrown = Color(0xFF543B25)
private val Green = Color(0xFF77964C)
private val Gold = Color(0xFFD5A33B)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { NileGame() }
    }
}

enum class BuildChoice(val title: String, val cost: Int) {
    FIELD("Termőföld", 15), HOUSE("Ház", 30), WORKSHOP("Műhely", 45), PYRAMID("Piramis", 100)
}

@Composable
fun NileGame() {
    var food by remember { mutableIntStateOf(80) }
    var gold by remember { mutableIntStateOf(120) }
    var people by remember { mutableIntStateOf(12) }
    var fields by remember { mutableIntStateOf(2) }
    var houses by remember { mutableIntStateOf(3) }
    var pyramid by remember { mutableIntStateOf(0) }
    var year by remember { mutableIntStateOf(1) }
    var message by remember { mutableStateOf("Üdvözöl a Nílus völgye! Építs települést a folyó partján.") }
    var selected by remember { mutableStateOf(BuildChoice.FIELD) }

    MaterialTheme {
        Column(
            Modifier.fillMaxSize().background(Sand).padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("NÍLUS VÖLGYE", fontSize = 25.sp, fontWeight = FontWeight.Black, color = DarkBrown)
            Text("Egy saját ókori városépítő játék első prototípusa", fontSize = 13.sp, color = DarkBrown)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                StatCard("Élelem", "🌾 $food", Modifier.weight(1f))
                StatCard("Arany", "🪙 $gold", Modifier.weight(1f))
                StatCard("Lakosok", "👥 $people", Modifier.weight(1f))
            }
            CityMap(fields, houses, pyramid, Modifier.fillMaxWidth().weight(1f))
            Text("Építés", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkBrown)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                BuildChoice.entries.forEach { choice ->
                    val active = selected == choice
                    Column(
                        Modifier.weight(1f)
                            .background(if (active) Gold else Papyrus, RoundedCornerShape(10.dp))
                            .clickable { selected = choice }
                            .padding(vertical = 10.dp, horizontal = 3.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            when (choice) {
                                BuildChoice.FIELD -> "🌾"
                                BuildChoice.HOUSE -> "🏠"
                                BuildChoice.WORKSHOP -> "🧱"
                                BuildChoice.PYRAMID -> "🔺"
                            }, fontSize = 21.sp
                        )
                        Text(choice.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DarkBrown)
                        Text("${choice.cost} arany", fontSize = 10.sp, color = DarkBrown)
                    }
                }
            }
            Button(
                onClick = {
                    if (gold < selected.cost) {
                        message = "Nincs elég arany. Várj a következő évig, vagy gyűjts élelmet!"
                    } else {
                        gold -= selected.cost
                        when (selected) {
                            BuildChoice.FIELD -> { fields++; message = "Új termőföldet hoztál létre. A következő évben több élelem terem." }
                            BuildChoice.HOUSE -> { houses++; people += 2; message = "Új ház épült, két új lakos költözött a városba." }
                            BuildChoice.WORKSHOP -> { gold += 5; message = "Műhelyt építettél. A kézművesek fellendítik a gazdaságot." }
                            BuildChoice.PYRAMID -> { pyramid++; message = "A piramis építése halad! Minden nagy építkezés a város dicsőségét növeli." }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBrown)
            ) { Text("${selected.title} építése (${selected.cost} arany)") }
            Button(
                onClick = {
                    year++
                    food += fields * 12 - people * 2
                    gold += houses * 4 + fields * 2 + pyramid * 3
                    if (food < 0) {
                        people = (people - 2).coerceAtLeast(4)
                        food = 0
                        message = "Szűkös volt az aratás: néhány lakos elköltözött. Építs több termőföldet!"
                    } else {
                        message = "Elérkezett a $year. év! A termés és a kereskedelem bevétele megérkezett."
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NileBlue)
            ) { Text("⏩ Következő év") }
            Text(message, fontSize = 12.sp, color = DarkBrown, modifier = Modifier.fillMaxWidth().background(Papyrus, RoundedCornerShape(8.dp)).padding(10.dp))
            Text("Év: $year  •  Saját fejlesztésű prototípus", fontSize = 10.sp, color = DarkBrown)
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier.background(Papyrus, RoundedCornerShape(10.dp)).padding(9.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, fontSize = 11.sp, color = DarkBrown)
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkBrown)
    }
}

@Composable
fun CityMap(fields: Int, houses: Int, pyramid: Int, modifier: Modifier = Modifier) {
    Canvas(modifier.background(Color(0xFFE8D09A), RoundedCornerShape(14.dp))) {
        val w = size.width
        val h = size.height
        // Desert base
        drawRect(Color(0xFFE8D09A), size = size)
        // Nile river, gently winding
        val river = Path().apply {
            moveTo(w * .63f, 0f)
            cubicTo(w * .45f, h * .20f, w * .76f, h * .34f, w * .58f, h * .52f)
            cubicTo(w * .42f, h * .72f, w * .68f, h * .82f, w * .50f, h)
            lineTo(w * .70f, h)
            cubicTo(w * .84f, h * .79f, w * .57f, h * .69f, w * .72f, h * .51f)
            cubicTo(w * .88f, h * .32f, w * .58f, h * .21f, w * .78f, 0f)
            close()
        }
        drawPath(river, NileBlue)
        // river highlights
        for (i in 0..6) {
            drawLine(Color(0xFF8BD0D4), Offset(w * (.62f + i * .012f), h * (.12f + i * .11f)),
                Offset(w * (.68f + i * .012f), h * (.16f + i * .11f)), strokeWidth = 3f)
        }
        // farm plots
        val farmCount = min(fields, 8)
        for (i in 0 until farmCount) {
            val x = w * (.05f + (i % 3) * .16f)
            val y = h * (.12f + (i / 3) * .20f)
            drawRect(Green, Offset(x, y), Size(w * .12f, h * .10f))
            for (r in 1..3) drawLine(Color(0xFFC7D58A), Offset(x + 5f, y + r * h * .022f),
                Offset(x + w * .11f, y + r * h * .022f), strokeWidth = 2f)
        }
        // houses
        val houseCount = min(houses, 8)
        for (i in 0 until houseCount) {
            val x = w * (.06f + (i % 3) * .16f)
            val y = h * (.55f + (i / 3) * .13f)
            drawRect(Color(0xFFC88A4A), Offset(x, y), Size(w * .10f, h * .075f))
            val roof = Path().apply {
                moveTo(x - 3f, y); lineTo(x + w * .05f, y - h * .045f); lineTo(x + w * .103f, y); close()
            }
            drawPath(roof, DarkBrown)
            drawRect(Color(0xFF704A2C), Offset(x + w * .04f, y + h * .035f), Size(w * .018f, h * .04f))
        }
        // pyramid(s)
        if (pyramid > 0) {
            val p = Path().apply {
                moveTo(w * .17f, h * .90f)
                lineTo(w * .30f, h * (.90f - min(pyramid, 3) * .11f))
                lineTo(w * .43f, h * .90f)
                close()
            }
            drawPath(p, Color(0xFFD8B36B))
            drawLine(Color(0xFFF5D995), Offset(w * .30f, h * .90f), Offset(w * .30f, h * (.90f - min(pyramid, 3) * .11f)), strokeWidth = 3f)
        }
        // small palm silhouettes
        for (i in 0..2) {
            val x = w * (.84f + i * .045f)
            val y = h * (.25f + i * .20f)
            drawLine(DarkBrown, Offset(x, y), Offset(x, y - h * .08f), strokeWidth = 4f)
            for (j in -2..2) {
                drawLine(Green, Offset(x, y - h * .08f), Offset(x + j * 8f, y - h * .12f), strokeWidth = 3f)
            }
        }
    }
}
