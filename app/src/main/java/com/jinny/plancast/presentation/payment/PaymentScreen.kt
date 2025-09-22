import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jinny.plancast.presentation.payment.PaymentUiState
import com.jinny.plancast.presentation.payment.PaymentViewModel
import com.jinny.plancast.presentation.weather.SearchViewModel
import com.jinny.plancast.presentation.weather.WeatherMode
import java.text.NumberFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel,
    state: PaymentUiState,
    onPayClick: () -> Unit,
    onMethodChangeClick: () -> Unit,
    onDismissRequest: () -> Unit,
    onMethodSelected: (String) -> Unit,
    showPaymentMethodDialog: Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("결제하기") },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: 뒤로 가기 */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로 가기")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // 주문 내역 섹션
                OrderSummarySection(itemName = state.itemName, price = state.price)

                Divider(modifier = Modifier.padding(vertical = 24.dp))

                // 결제 수단 섹션
                PaymentMethodSection(
                    selectedMethod = state.selectedPaymentMethod,
                    onChangeClick = onMethodChangeClick
                )
            }

            // 결제 버튼 (화면 하단에 고정)
            PayButton(
                price = state.price,
                onClick = onPayClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )

            // 결제 수단 변경 다이얼로그
            if (showPaymentMethodDialog) {
                PaymentMethodDialog(
                    methods = state.availablePaymentMethods,
                    onDismissRequest = onDismissRequest,
                    onMethodSelected = onMethodSelected
                )
            }
        }
    }
}

@Composable
fun OrderSummarySection(itemName: String, price: Int) {
    Column {
        Text("주문 내역", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(itemName, style = MaterialTheme.typography.bodyLarge)
            Text("${NumberFormat.getNumberInstance(Locale.KOREA).format(price)}원", style = MaterialTheme.typography.bodyLarge)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("총 결제 금액", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(
                "${NumberFormat.getNumberInstance(Locale.KOREA).format(price)}원",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun PaymentMethodSection(selectedMethod: String, onChangeClick: () -> Unit) {
    Column {
        Text("결제 수단", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = "결제 수단 아이콘", tint = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(selectedMethod, style = MaterialTheme.typography.bodyLarge)
            }
            TextButton(onClick = onChangeClick) {
                Text("변경")
            }
        }
    }
}

@Composable
fun PayButton(price: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        Text(
            "${NumberFormat.getNumberInstance(Locale.KOREA).format(price)}원 결제하기",
            fontSize = 18.sp
        )
    }
}

@Composable
fun PaymentMethodDialog(
    methods: List<String>,
    onDismissRequest: () -> Unit,
    onMethodSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("결제 수단 선택") },
        text = {
            LazyColumn {
                items(methods) { method ->
                    Text(
                        text = method,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMethodSelected(method) }
                            .padding(vertical = 12.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("닫기")
            }
        }
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    var showDialog by remember { mutableStateOf(false) }
    var state by remember { mutableStateOf(PaymentUiState()) }

    val fakeViewModel = PaymentViewModel(
        id = -1
    )

    MaterialTheme {
        PaymentScreen(
            viewModel = fakeViewModel,
            state = state,
            onPayClick = { /*TODO*/ },
            onMethodChangeClick = { showDialog = true },
            onDismissRequest = { showDialog = false },
            onMethodSelected = {
                state = state.copy(selectedPaymentMethod = it)
                showDialog = false
            },
            showPaymentMethodDialog = showDialog
        )
    }
}