package com.autoclick.tool.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.autoclick.tool.R
import com.autoclick.tool.data.model.ClickPoint
import com.autoclick.tool.presentation.theme.AutoClickToolTheme
import com.autoclick.tool.presentation.viewmodel.MainViewModel
import com.autoclick.tool.service.AutoClickAccessibilityService
import com.autoclick.tool.utils.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val viewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            AutoClickToolTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val clickPoints by viewModel.clickPoints.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }
    
    // Check permissions
    val hasOverlayPermission = PermissionUtils.hasOverlayPermission(context)
    val hasAccessibilityPermission = PermissionUtils.isAccessibilityServiceEnabled(
        context, 
        AutoClickAccessibilityService::class.java
    )
    
    LaunchedEffect(Unit) {
        if (!hasOverlayPermission || !hasAccessibilityPermission) {
            showPermissionDialog = true
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Status indicators
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatusChip(
                        label = "Dịch vụ",
                        isActive = uiState.isServiceRunning,
                        modifier = Modifier.weight(1f)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    StatusChip(
                        label = "Auto Click",
                        isActive = uiState.isAutoClickRunning,
                        modifier = Modifier.weight(1f)
                    )
                }
                
                // Session info
                uiState.currentSession?.let { session ->
                    if (session.isRunning) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Đã click: ${session.totalClicks} lần",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Control buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    if (uiState.isAutoClickRunning) {
                        viewModel.stopAutoClick()
                    } else {
                        viewModel.startAutoClick()
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = uiState.isServiceRunning && clickPoints.any { it.isEnabled }
            ) {
                Icon(
                    imageVector = if (uiState.isAutoClickRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    if (uiState.isAutoClickRunning) 
                        stringResource(R.string.stop) 
                    else 
                        stringResource(R.string.start)
                )
            }
            
            OutlinedButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(stringResource(R.string.add_point))
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Click points list
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.click_points_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    if (clickPoints.isNotEmpty()) {
                        TextButton(
                            onClick = { viewModel.deleteAllClickPoints() }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Xóa tất cả")
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                if (clickPoints.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_click_points),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn {
                        items(clickPoints) { clickPoint ->
                            ClickPointItem(
                                clickPoint = clickPoint,
                                onToggle = { viewModel.toggleClickPointEnabled(clickPoint.id, !clickPoint.isEnabled) },
                                onEdit = { /* TODO: Implement edit */ },
                                onDelete = { viewModel.deleteClickPoint(clickPoint) }
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Show messages
    uiState.message?.let { message ->
        LaunchedEffect(message) {
            // Show snackbar or toast
            viewModel.clearMessage()
        }
    }
    
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            // Show error snackbar or toast
            viewModel.clearError()
        }
    }
    
    // Dialogs
    if (showAddDialog) {
        AddClickPointDialog(
            onDismiss = { showAddDialog = false },
            onSave = { clickPoint ->
                viewModel.addClickPoint(clickPoint)
                showAddDialog = false
            }
        )
    }
    
    if (showPermissionDialog) {
        PermissionDialog(
            hasOverlayPermission = hasOverlayPermission,
            hasAccessibilityPermission = hasAccessibilityPermission,
            onDismiss = { showPermissionDialog = false },
            onRequestOverlay = {
                PermissionUtils.requestOverlayPermission(context)
            },
            onRequestAccessibility = {
                PermissionUtils.requestAccessibilityPermission(context)
            }
        )
    }
}

@Composable
fun StatusChip(
    label: String,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun ClickPointItem(
    clickPoint: ClickPoint,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = clickPoint.isEnabled,
                onCheckedChange = { onToggle() }
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = clickPoint.name,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "X: ${clickPoint.x.toInt()}, Y: ${clickPoint.y.toInt()}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Độ trễ: ${clickPoint.delayMs}ms, Lặp: ${clickPoint.repeatCount}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Chỉnh sửa")
            }
            
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Xóa")
            }
        }
    }
}

@Composable
fun AddClickPointDialog(
    onDismiss: () -> Unit,
    onSave: (ClickPoint) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var x by remember { mutableStateOf("") }
    var y by remember { mutableStateOf("") }
    var delay by remember { mutableStateOf("1000") }
    var repeat by remember { mutableStateOf("1") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm điểm nhấp") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên điểm") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row {
                    OutlinedTextField(
                        value = x,
                        onValueChange = { x = it },
                        label = { Text("X") },
                        modifier = Modifier.weight(1f)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    OutlinedTextField(
                        value = y,
                        onValueChange = { y = it },
                        label = { Text("Y") },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = delay,
                    onValueChange = { delay = it },
                    label = { Text("Độ trễ (ms)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = repeat,
                    onValueChange = { repeat = it },
                    label = { Text("Số lần lặp") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && x.isNotBlank() && y.isNotBlank()) {
                        val clickPoint = ClickPoint(
                            name = name,
                            x = x.toFloatOrNull() ?: 0f,
                            y = y.toFloatOrNull() ?: 0f,
                            delayMs = delay.toLongOrNull() ?: 1000L,
                            repeatCount = repeat.toIntOrNull() ?: 1
                        )
                        onSave(clickPoint)
                    }
                }
            ) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}

@Composable
fun PermissionDialog(
    hasOverlayPermission: Boolean,
    hasAccessibilityPermission: Boolean,
    onDismiss: () -> Unit,
    onRequestOverlay: () -> Unit,
    onRequestAccessibility: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cần cấp quyền") },
        text = {
            Column {
                Text("Ứng dụng cần các quyền sau để hoạt động:")
                
                Spacer(modifier = Modifier.height(8.dp))
                
                if (!hasOverlayPermission) {
                    Text("• Quyền hiển thị trên ứng dụng khác")
                }
                
                if (!hasAccessibilityPermission) {
                    Text("• Dịch vụ hỗ trợ (Accessibility)")
                }
            }
        },
        confirmButton = {
            Column {
                if (!hasOverlayPermission) {
                    TextButton(onClick = onRequestOverlay) {
                        Text("Cấp quyền Overlay")
                    }
                }
                
                if (!hasAccessibilityPermission) {
                    TextButton(onClick = onRequestAccessibility) {
                        Text("Cấp quyền Accessibility")
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Để sau")
            }
        }
    )
}
