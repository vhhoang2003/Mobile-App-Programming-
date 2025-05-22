    package com.example.myapplication
    
    import android.Manifest
    import android.content.Context
    import android.content.pm.PackageManager
    import android.net.ConnectivityManager
    import android.net.LinkProperties
    import android.net.NetworkCapabilities
    import android.net.wifi.ScanResult
    import android.net.wifi.WifiManager
    import android.os.Build
    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.activity.compose.rememberLauncherForActivityResult
    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.rememberScrollState
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.foundation.verticalScroll
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.Refresh
    import androidx.compose.material.icons.filled.Wifi
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.core.app.ActivityCompat
    import com.example.myapplication.ui.theme.MyApplicationTheme
    import java.net.Inet4Address
    
    class MainActivity : ComponentActivity() {
    
        private lateinit var wifiManager: WifiManager
    
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    
            setContent {
                MyApplicationTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        WifiInfoScreen(wifiManager)
                    }
                }
            }
        }
    }
    
    @Composable
    fun WifiInfoScreen(wifiManager: WifiManager) {
        val context = LocalContext.current
        var permissionGranted by remember { mutableStateOf(false) }
        var currentWifiInfo by remember { mutableStateOf("") }
        var wifiListInfo by remember { mutableStateOf(listOf<String>()) }
        var extraNetworkInfo by remember { mutableStateOf("") }
        var bestSuggestion by remember { mutableStateOf("") }
    
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            permissionGranted = granted
            if (granted) {
                loadWifiData(wifiManager, context) {
                    currentWifiInfo = it.first
                    wifiListInfo = it.second.split("\n\n")
                    extraNetworkInfo = it.third
                    bestSuggestion = suggestBestNetwork(wifiManager.scanResults)
                }
            }
        }
    
        LaunchedEffect(true) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                permissionGranted = true
                loadWifiData(wifiManager, context) {
                    currentWifiInfo = it.first
                    wifiListInfo = it.second.split("\n\n")
                    extraNetworkInfo = it.third
                    bestSuggestion = suggestBestNetwork(wifiManager.scanResults)
                }
            }
        }
    
        WifiAppUI(
            currentInfo = currentWifiInfo,
            wifiList = wifiListInfo,
            networkInfo = extraNetworkInfo,
            bestSuggestion = bestSuggestion,
            onRefresh = {
                loadWifiData(wifiManager, context) {
                    currentWifiInfo = it.first
                    wifiListInfo = it.second.split("\n\n")
                    extraNetworkInfo = it.third
                    bestSuggestion = suggestBestNetwork(wifiManager.scanResults)
                }
            }
        )
    }
    
    fun suggestBestNetwork(scanResults: List<ScanResult>): String {
        return scanResults
            .filter { it.capabilities.contains("WPA") || it.capabilities.contains("WPA2") || it.capabilities.contains("WPA3") }
            .maxByOrNull { it.level }?.let {
                "✅ Gợi ý kết nối: \nSSID: ${it.SSID}\nTín hiệu: ${it.level} dBm\nBảo mật: ${it.capabilities}"
            } ?: "❌ Không tìm thấy mạng phù hợp với tiêu chí an toàn và mạnh."
    }
    
    fun loadWifiData(
        wifiManager: WifiManager,
        context: Context,
        onWifiInfoLoaded: (Triple<String, String, String>) -> Unit
    ) {
        val info = wifiManager.connectionInfo
    
        val wifiInfo = """
            📶 Wi-Fi hiện tại:
            - SSID: ${info.ssid}
            - BSSID (MAC Access Point): ${info.bssid}
            - IP Address: ${intToIp(info.ipAddress)}
            - MAC Address (thiết bị): ${info.macAddress}
            - Link Speed: ${info.linkSpeed} Mbps
            - RSSI: ${info.rssi} dBm
            - Network ID: ${info.networkId}
            - Frequency: ${info.frequency} MHz
            - Hidden SSID: ${info.hiddenSSID}
            - Supplicant State: ${info.supplicantState}
            - Is Passpoint AP: ${getPasspointStatus(info)}
            - Is 2.4GHz: ${info.frequency in 2400..2500}
            - Is 5GHz: ${info.frequency in 4900..5900}
        """.trimIndent()
    
        val scanResults = wifiManager.scanResults
        val wifiList = scanResults.joinToString("\n\n") {
            "SSID: ${it.SSID}\nBSSID: ${it.BSSID}\nRSSI: ${it.level} dBm\nTần số: ${it.frequency} MHz\nBảo mật: ${it.capabilities}"
        }
    
        val networkDetails = getNetworkDetails(context)
    
        onWifiInfoLoaded(Triple(wifiInfo, wifiList, networkDetails))
    }
    
    fun getPasspointStatus(info: android.net.wifi.WifiInfo): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            runCatching {
                val method = info.javaClass.getMethod("isPasspointAp")
                method.invoke(info).toString()
            }.getOrDefault("Không xác định")
        } else {
            "Không hỗ trợ (API < 30)"
        }
    }
    
    fun getNetworkDetails(context: Context): String {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return "❗ Không tìm thấy kết nối mạng"
        val props: LinkProperties = cm.getLinkProperties(network) ?: return "❗ Không lấy được LinkProperties"
        val capabilities = cm.getNetworkCapabilities(network)
    
        val dnsServers = props.dnsServers.joinToString { it.hostAddress ?: "?" }
        val gateway = props.routes.find { it.gateway is Inet4Address }?.gateway?.hostAddress ?: "?"
        val interfaceName = props.interfaceName ?: "?"
        val subnet = props.linkAddresses.find { it.address is Inet4Address }?.prefixLength?.let { "/$it" } ?: "?"
    
        return """
            🌐 Thông tin mạng bổ sung:
            - DNS: $dnsServers
            - Gateway: $gateway
            - Subnet Mask: $subnet
            - Interface: $interfaceName
            - Internet Capable: ${capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true}
        """.trimIndent()
    }
    
    fun intToIp(ip: Int): String {
        return "${ip and 0xFF}.${ip shr 8 and 0xFF}.${ip shr 16 and 0xFF}.${ip shr 24 and 0xFF}"
    }
    
    @Composable
    fun WifiAppUI(
        currentInfo: String,
        wifiList: List<String>,
        networkInfo: String,
        bestSuggestion: String,
        onRefresh: () -> Unit
    ) {
        val scrollState = rememberScrollState()
    
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Wifi,
                    contentDescription = "Wi-Fi Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Wi-Fi Insight",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
    
            // AI Suggestion
            SectionCard(title = "🤖 AI Gợi ý mạng tốt nhất", content = bestSuggestion)
    
            // Sections
            SectionCard(title = "📶 Wi-Fi đang kết nối", content = currentInfo)
            SectionCard(title = "🌐 Thông tin mạng bổ sung", content = networkInfo)
    
            Text(
                text = "📡 Các Wi-Fi xung quanh",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            wifiList.forEach { wifi ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Text(
                        text = wifi,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
    
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRefresh) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Quét lại Wi-Fi")
            }
        }
    }
    
    @Composable
    fun SectionCard(title: String, content: String) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = content,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
