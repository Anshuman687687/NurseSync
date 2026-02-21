package exceptionallybad.nursesync.feature.discharge.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val CyanPrimary = Color(0xFF00C3C0)
val CyanLight = Color(0xFFE5F7F7)
val TextGray = Color(0xFF8E9AAA)
val TextDark = Color(0xFF1D2939)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DischargeSummaryScreen(
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Discharge Summary",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = TextDark
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = TextDark)
                    }
                },
                actions = {
                    IconButton(onClick = onShareClick) {
                        Icon(Icons.Default.Share, "Share", tint = CyanPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                )
            )
        },
        containerColor = Color(0xFFF9F9FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            Spacer(Modifier.height(16.dp))
            PatientInfoCard()
            Spacer(Modifier.height(28.dp))
            SynthesisStatusSection()
            Spacer(Modifier.height(16.dp))
            GenerateSummaryButton()
            Spacer(Modifier.height(36.dp))
            DocumentPreviewSection()
            Spacer(Modifier.height(32.dp))
            BottomActionRow()
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun PatientInfoCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Patient Avatar Stack
            Box(
                modifier = Modifier.size(64.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(CyanLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = CyanPrimary,
                        modifier = Modifier.size(40.dp)
                    )
                }
                // Online/Status indicator
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF22C55E))
                        .border(2.dp, Color.White, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Robert Sterling",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextDark
                )
                Text(
                    text = "ID: #882910 • Room 402-B",
                    fontSize = 12.sp,
                    color = TextGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BadgeChip(text = "ADMITTED OCT 24", outlined = false)
                    BadgeChip(text = "READY FOR REVIEW", outlined = true, textColor = CyanPrimary)
                }
            }
        }
    }
}

@Composable
fun BadgeChip(text: String, outlined: Boolean, textColor: Color = TextGray) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (outlined) CyanLight else Color(0xFFF1F4F8))
            .border(
                width = if (outlined) 1.dp else 0.dp,
                color = if (outlined) CyanPrimary.copy(alpha = 0.3f) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun SynthesisStatusSection() {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SYNTHESIS STATUS",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = TextGray,
                letterSpacing = 1.sp
            )
            Text(
                text = "75% Complete",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = CyanPrimary
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        StatusItemCard(
            title = "Clinical Course & History",
            subtitle = "Extracted from 14 voice logs",
            icon = Icons.Rounded.CheckCircle,
            iconColor = CyanPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        StatusItemCard(
            title = "Treatments & Labs",
            subtitle = "All metrics updated today",
            icon = Icons.Rounded.CheckCircle,
            iconColor = CyanPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        StatusItemCard(
            title = "Medication Reconciliation",
            subtitle = "2 pending confirmations",
            icon = Icons.Default.MoreHoriz, // Reusing dots for simplicity
            iconColor = Color.White,
            iconBgColor = CyanPrimary,
            highlightBorder = true,
            actionButtonText = "FIX"
        )
    }
}

@Composable
fun StatusItemCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    iconBgColor: Color = Color.Transparent,
    highlightBorder: Boolean = false,
    actionButtonText: String? = null
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .let { if (highlightBorder) it.border(2.dp, CyanPrimary, RoundedCornerShape(12.dp)) else it }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(if (iconBgColor != Color.Transparent) 16.dp else 24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextDark
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = if (highlightBorder) CyanPrimary else TextGray
                )
            }
            if (actionButtonText != null) {
                TextButton(onClick = {}) {
                    Text(
                        text = actionButtonText,
                        color = CyanPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun GenerateSummaryButton() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF0F9C98), Color(0xFF1BE1DB))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Generate AI Summary",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "AI will compile voice notes, vitals, and physician orders into a\nstructured medical document.",
            color = TextGray,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            fontStyle = FontStyle.Italic,
            lineHeight = 16.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun DocumentPreviewSection() {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "DOCUMENT PREVIEW",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = TextGray,
                letterSpacing = 1.sp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "EDIT DRAFT",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = CyanPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Paper Card
        Card(
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "OFFICIAL MEDICAL RECORD",
                    fontSize = 10.sp,
                    color = TextGray,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "St. Mary's Medical Center",
                    fontSize = 20.sp,
                    color = TextDark,
                    fontStyle = FontStyle.Italic, // Normally uses a serif font, simulating with italic
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = Color(0xFFF1F4F8))
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "CLINICAL COURSE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Patient was admitted following an acute exacerbation of COPD. Initial treatment included IV methylprednisolone and nebulized bronchodilators. Oxygen saturation improved from 88% to 94% on room air over 72 hours.",
                    fontSize = 13.sp,
                    color = Color(0xFF475467),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "DISCHARGE MEDICATIONS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                MedicationBullet("Prednisone 40mg PO daily (5-day taper)")
                MedicationBullet("Salmeterol/Fluticasone 250/50 1 puff BID")
                MedicationBullet("Albuterol 90mcg Inhaler PRN")

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "FOLLOW-UP INSTRUCTIONS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Patient to follow up with primary care physician within 7 days. Return to ED if experiencing increased shortness of breath or fever >101.5°F.",
                    fontSize = 13.sp,
                    color = Color(0xFF475467),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))
                HorizontalDivider(color = Color(0xFFF1F4F8)) 
                
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text("PHYSICIAN SIGNATURE", fontSize = 9.sp, color = TextGray, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.height(1.dp).width(120.dp).background(Color(0xFFD0D5DD)))
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("DATE GENERATED", fontSize = 9.sp, color = TextGray, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Oct 27, 2023", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    }
                }
            }
        }
    }
}

@Composable
fun MedicationBullet(text: String) {
    Row(modifier = Modifier.padding(bottom = 8.dp), verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .padding(top = 6.dp)
                .size(6.dp)
                .clip(CircleShape)
                .background(CyanPrimary)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = Color(0xFF475467),
            lineHeight = 20.sp
        )
    }
}

@Composable
fun BottomActionRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActionButton(icon = Icons.Default.PictureAsPdf, label = "PDF", outlined = true, modifier = Modifier.weight(1f))
        ActionButton(icon = Icons.Default.Print, label = "FAX", outlined = true, modifier = Modifier.weight(1f))
        ActionButton(
            icon = Icons.Default.CloudUpload, 
            label = "SAVE TO EHR", 
            outlined = false,
            modifier = Modifier.weight(1.5f)
        )
    }
}

@Composable
fun ActionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, outlined: Boolean, modifier: Modifier = Modifier) {
    Button(
        onClick = {},
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (outlined) Color(0xFFF1F4F8) else CyanPrimary, // Actually the image has cyan light for PDF.
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon, 
                contentDescription = null, 
                tint = if (outlined) CyanPrimary else Color.White,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = if (outlined) TextDark else Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}
