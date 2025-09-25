import android.annotation.SuppressLint
import android.widget.Toast

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jinny.plancast.data.model.TransferResponse
import com.jinny.plancast.domain.repository.TransferRepository
import com.jinny.plancast.domain.transferUseCase.TransferMoneyUseCase
import com.jinny.plancast.presentation.login.LoginViewModel
import com.jinny.plancast.presentation.transfer.TransferViewModel


@Composable
fun TransferScreen(viewModel: TransferViewModel) {
    val context = LocalContext.current
//    val transferResult by viewModel..observeAsState()

   //  ViewModel의 LiveData를 관찰하여 Toast 메시지 띄우기
//    LaunchedEffect(transferResult) {
//        if (!transferResult.isNullOrEmpty()) {
//            Toast.makeText(context, transferResult, Toast.LENGTH_LONG).show()
//        }
//    }

    // UI 상태 관리
    var recipient by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = recipient,
            onValueChange = { recipient = it },
            label = { Text("계좌번호") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("송금 금액") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val parsedAmount = amount.toIntOrNull()
                if (recipient.isNotEmpty() && parsedAmount != null) {
//                    viewModel.transferMoney(recipient, parsedAmount)
                    viewModel.goToPassword()
                } else {
                    Toast.makeText(context, "계좌번호와 금액을 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "송금하기")
        }
    }
}

private class FakeTransferRepository : TransferRepository {
    override suspend fun transferMoney(
        recipient: String, amount: Int
    ): TransferResponse {

        return TransferResponse(
            status = "success",
            message = "송금이 완료되었습니다."
        )
    }
}

private class FakeTransferUseCase : TransferMoneyUseCase(
    transferRepository = FakeTransferRepository()
) {
    override suspend fun execute(
        recipient: String, amount: Int
    ): TransferResponse {
        return TransferResponse(
            status = "success",
            message = "송금이 완료되었습니다."
        )
    }
}



@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun TransferScreenPreview() {

    val fakeViewModel = TransferViewModel(
        transferMoneyUseCase = FakeTransferUseCase()
    )

    // ViewModel 없이 프리뷰만 보기 위한 더미 호출
    TransferScreen(viewModel = fakeViewModel)
}