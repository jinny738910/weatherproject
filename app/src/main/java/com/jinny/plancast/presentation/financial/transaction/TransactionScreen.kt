import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinny.plancast.data.model.Product
import com.jinny.plancast.domain.usecase.productUseCase.CreateProductsUseCase
import com.jinny.plancast.domain.usecase.productUseCase.DeleteProductsUseCase
import com.jinny.plancast.domain.usecase.productUseCase.GetProductsByIdUseCase
import com.jinny.plancast.domain.usecase.productUseCase.GetProductsUseCase
import com.jinny.plancast.domain.usecase.productUseCase.UpdateProductsUseCase
import com.jinny.plancast.domain.repository.BackendRepository
import com.jinny.plancast.domain.repository.ProductRepository
import com.jinny.plancast.presentation.financial.payment.PaymentState
import com.jinny.plancast.presentation.financial.transaction.TransactionState

import com.jinny.plancast.presentation.financial.transaction.TransactionViewModel
import java.text.NumberFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel
) {
    // ViewModel의 StateFlow를 Compose State로 수집
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchData()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("금융 거래 목록") }) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is TransactionState.Success -> {
                    // 로딩 상태 표시
                    val products = (uiState as TransactionState.Success).products
                    ProductList(products = products)
                }

                is TransactionState.Error -> {
                    // 에러 상태 표시
                    Text(
                        text = "오류: ${(uiState as TransactionState.Error).message}",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    // (옵션) 재시도 버튼 추가 가능
                }

                TransactionState.Idle -> {
                    // 성공적으로 데이터를 받아 리스트 표시
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                TransactionState.Loading -> {
                    // 초기 상태 (데이터 로드 대기)
                    Text(
                        text = "상품 정보를 불러오는 중...",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductList(products: List<Product>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductListItem(product = product)
            Divider() // 각 항목 아래 구분선
        }
    }
}

@Composable
fun ProductListItem(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* 상품 상세 화면으로 이동 로직 */ },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // 상품 이름
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                // 상품 ID
                Text(
                    text = "ID: ${product.id ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 상품 가격
            Text(
                text = formatPrice(product.price),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

fun formatPrice(price: Int): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.KOREA)
    return formatter.format(price)
}


@Suppress("UNREACHABLE_CODE")
private class FakeProductRepository2 : ProductRepository {
    override suspend fun createProduct(product: Product): Product {
        return product.copy(id = 1L)
    }

    override suspend fun getProducts(): List<Product> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductById(id: Long): Product {
        return Product(
            id = id, name = "Fake Product", price = 1000,
            selectedPaymentMethod = TODO(),
            availablePaymentMethods = TODO()
        )
    }

    override suspend fun updateProduct(id: Long, product: Product): Product {
        return product.copy(id = id)
    }

    override suspend fun deleteProduct(id: Long) {

    }
}


private class FakeCreateProductsUseCase2 : CreateProductsUseCase(
    repository = FakeProductRepository2()
)

private class FakeDeleteProductsUseCase2 : DeleteProductsUseCase(
    repository = FakeProductRepository2()
)

private class FakeGetProductsByIdUseCase2 : GetProductsByIdUseCase(
    repository = FakeProductRepository2()
)

private class FakeGetProductsUseCase2 : GetProductsUseCase(
    repository = FakeProductRepository2()
)

private class FakeUpdateProductsUseCase2 : UpdateProductsUseCase(
    repository = FakeProductRepository2()
)

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun TransactionScreenPreview() {
    var showDialog by remember { mutableStateOf(false) }
    var state by remember { mutableStateOf(PaymentState()) }

    val fakeViewModel = TransactionViewModel(
        backendRepository = object : BackendRepository {
            override suspend fun fetchHelloMessage(): Result<String> {
                return Result.success("Hello from Fake Repository")
            }
        },
        createProductUsecase = FakeCreateProductsUseCase2(),
        deleteProductsUseCase = FakeDeleteProductsUseCase2(),
        getProductsByIdUseCase = FakeGetProductsByIdUseCase2(),
        getProductsUseCase = FakeGetProductsUseCase2(),
        updateProductsUseCase = FakeUpdateProductsUseCase2()
    )

//    val fakeViewModel = LoginViewModel(
//        id = 1L
//    )

    MaterialTheme {
        TransactionScreen(
            viewModel = fakeViewModel
        )
    }
}