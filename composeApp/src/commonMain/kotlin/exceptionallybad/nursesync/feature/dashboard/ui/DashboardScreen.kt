package exceptionallybad.nursesync.feature.dashboard.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import exceptionallybad.nursesync.feature.dashboard.viewmodel.DashboardViewModel
import org.koin.compose.viewmodel.koinViewModel

private val CyanPrimary = Color(0xFF00C3C0)
private val CardBg = Color(0xFFF9FAFB)
private val TextGray = Color(0xFF667085)
private val TextDark = Color(0xFF101828)
private val SuccessGreen = Color(0xFF12B76A)
private val WarningOrange = Color(0xFFF79009)
private val ErrorRed = Color(0xFFF04438)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = koinViewModel(),
) {
    Scaffold(
        topBar = {
            DashboardTopBar()
        },
        containerColor = Color.White
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                StatsSection()
            }
            
            item {
                SectionHeader(title = "AI INSIGHTS")
                Spacer(modifier = Modifier.height(12.dp))
                AiInsightCard()
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Patients",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    FilterTabs()
                }
            }
            
            item {
                PatientStatusCard(
                    room = "302",
                    name = "Sarah Johnson",
                    tag = "Post-Op Recovery • High Risk",
                    status = "ACTIVE CHECK",
                    vitals = listOf("115", "142/90", "96%"),
                    isAlert = true
                )
            }
            
            item {
                PatientStatusCard(
                    room = "405",
                    name = "Marcus Chen",
                    tag = "Pneumonia • Stable",
                    status = "LAST CHECKED 45M",
                    vitals = null,
                    bottomContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CardBg, RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = TextDark,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Morning Meds Administered", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                )
            }

            item {
                PatientStatusCard(
                    room = "211",
                    name = "Elena Rodriguez",
                    tag = "Post-Op Recovery • Routine",
                    vitals = null,
                    bottomContent = {
                         Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CardBg, RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = TextDark,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("UPCOMING TASK", fontSize = 10.sp, color = TextGray, fontWeight = FontWeight.Bold)
                                    Text("Wound Care Dressing Change", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                                Text("09:30 AM", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(CyanPrimary.copy(alpha = 0.1f))) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = CyanPrimary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("EVENING SHIFT • ICU", fontSize = 10.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                    Text("NurseSync", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
                }
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Rounded.Search, contentDescription = "Search")
            }
            IconButton(onClick = {}) {
                Box {
                    Icon(Icons.Rounded.Notifications, contentDescription = "Notifications")
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray) // Badge color in mock is gray/white
                            .align(Alignment.TopEnd)
                            .border(1.5.dp, Color.White, CircleShape)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Composable
fun StatsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(modifier = Modifier.weight(1f), title = "PATIENTS", value = "12", subValue = "↗ +2 today")
        StatCard(modifier = Modifier.weight(1f), title = "ACTIVE", value = "2", subValue = "❗ Logs req.")
        StatCard(modifier = Modifier.weight(1f), title = "SHIFT", value = "65%", isProgress = true)
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    subValue: String? = null,
    isProgress: Boolean = false
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF2F4F7))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontSize = 10.sp, color = TextGray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Spacer(modifier = Modifier.height(4.dp))
            if (isProgress) {
                LinearProgressIndicator(
                    progress = 0.65f,
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFFECFDF3),
                    trackColor = Color(0xFFF2F4F7)
                )
            } else if (subValue != null) {
                Text(subValue, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = TextDark)
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = SuccessGreen,
        letterSpacing = 1.sp
    )
}

@Composable
fun AiInsightCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF2F4F7))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(CardBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = TextDark)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("SYSTEM NOTIFICATION", fontSize = 10.sp, color = TextGray, fontWeight = FontWeight.Bold)
                        Text("2m ago", fontSize = 10.sp, color = TextGray)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Patient in 302: Heart rate trending upward (115 bpm)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "AI detected anomaly in vitals. Immediate check recommended for Room 302.",
                fontSize = 14.sp,
                color = TextGray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) // Spacer
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CardBg),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text("Dismiss", color = TextDark, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun FilterTabs() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(CardBg)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text("All", fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
        }
        Text("Alerts", fontSize = 12.sp, color = TextGray, modifier = Modifier.padding(horizontal = 12.dp))
    }
}

@Composable
fun PatientStatusCard(
    room: String,
    name: String,
    tag: String,
    status: String? = null,
    vitals: List<String>? = null,
    isAlert: Boolean = false,
    bottomContent: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, if (isAlert) CyanPrimary.copy(alpha = 0.5f) else Color(0xFFF2F4F7))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBg),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("ROOM", fontSize = 8.sp, color = TextGray, fontWeight = FontWeight.Bold)
                        Text(room, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Text(tag, fontSize = 12.sp, color = TextGray)
                }
                if (status != null) {
                    Text(
                        status,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isAlert) TextDark else TextGray
                    )
                }
            }
            
            if (vitals != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    VitalItem("BPM", vitals[0], true)
                    VitalItem("BP", vitals[1])
                    VitalItem("SPO2", vitals[2])
                }
            }
            
            if (bottomContent != null) {
                Spacer(modifier = Modifier.height(16.dp))
                bottomContent()
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                   Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(WarningOrange.copy(alpha = 0.2f))) {
                       Icon(Icons.Default.MedicalServices, contentDescription = null, tint = WarningOrange, modifier = Modifier.size(16.dp).align(Alignment.Center))
                   }
                   Icon(Icons.Default.ShowChart, contentDescription = null, tint = TextGray, modifier = Modifier.size(24.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("View Records", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = TextDark, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun VitalItem(label: String, value: String, isWarning: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 9.sp, color = TextGray, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
            if (isWarning) {
                Icon(Icons.Default.ShowChart, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(12.dp))
            }
        }
    }
}
