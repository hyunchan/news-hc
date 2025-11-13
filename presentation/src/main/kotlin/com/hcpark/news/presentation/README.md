# Presentation
> 화면을 구현할 때는 다음을 반드시 준수하여 작성합니다.
> MVIViewModel 등 컴포넌트들에 대해서는 `/Users/hcpark/StudioProjects/MyApplication/presentation/src/main/kotlin/com/hcpark/news/presentation/component/README.md` 참조
> AI 프롬프트에 작성한 화면 이름은 {view} 를 치환합니다.

## 화면 규약(Contract) 에 대해

* 파일명 및 클래스는 {view}Contract.kt 로 명명합니다. `ex. NewsContract.kt`
* 패키지 구조는 `presentation.{view}.contract` 로 합니다.
* 화면(view) 과 뷰모델(ViewModel) 에 대한 MVI 규약
* object 로 선언하고 내부에 각 규약에 대해 정의합니다.
* 규약에 요소로는 상태(UiState), 이벤트(UiEvent), 이펙트(UiEffect) 의 구현을 반드시 포함해야 합니다.
* 추가로 ModalState 등의 부수적인 규약을 추가할 수 있습니다

```kotlin
// example 
// package *.presentation.view.contract
//
// import com.hcpark.news.presentation.component.UiEffect
// import com.hcpark.news.presentation.component.UiEvent
// import com.hcpark.news.presentation.component.UiState

object ViewContract {
    data class State(
        val modalState: ModalState = ModalState.Dismiss,
    ) : UiState

    sealed class Event : UiEvent {
        data object OnRefresh : Event()
        data object OnModalDismiss : Event()
    }

    sealed class Effect : UiEffect {
        data class Launch(val route: String) : Effect()
    }

    //optional
    sealed class ModalState {
        data object None : ModalState()
    }
}
```

## 뷰모델(ViewModel, vm)에 대해

* 파일명 및 클래스는 {view}ViewModel.kt 로 명명합니다. `ex. NewsViewModel.kt`
* 패키지 구조는 presentation.{view}.viewmodel 로 합니다.
* 도메인과 뷰에 대한 연결(binding) 을 담당합니다.
* MVIViewModel 을 상속받아 구현해야하고 같은 화면 이름의 규약(Contract) 와 1:1 관계
* 규약에 접근할 때는 import *.presentation.view.contract.ViewContract.State &
  Event & Effect 수준으로 임포트 하여 가독성을 높이길 권장합니다.
* init 블록이 필요하다면 최상위에 구현합니다. 이후 오버라이드 함수를 구현합니다.
* 그 외 다른 함수들은 MVI 흐름을 위반하지 않도록 private 로 구현해야하며 우회가 필요한 경우 주석으로 명시합니다.
* 오버라이드 함수 handleEvent 는 이벤트 규약에 대해 when 문을 사용해 구현합니다.
* 각 이벤트는 함수를 트리거 하는 것으로 구현하고 그 때 함수의 이름은 이벤트의 이름을 반영하는 것보다 함수 자체의 기능을 표현해야 합니다.
    * Event.OnRefresh 가 실행하는 함수가 `fun onRefresh(), fun refresh()` 보다는 `fun fetch()` 가 더 적절한 이름입니다.

```kotlin
// example
// package *.presentation.view.viewmodel
//
// import com.hcpark.news.presentation.component.MVIViewModel
// import *.presentation.view.contract.ViewContract.State   권장
// import *.presentation.view.contract.ViewContract.Event   권장
// import *.presentation.view.contract.ViewContract.Effect  권장
// import *.presentation.view.contract.ViewContract.ModalState 권장

@HiltViewModel
class ViewViewModel @Inject constructor(
    private val fetchDataUseCase: FetchDataUseCase,
) : MVIViewModel<Event, State, Effect>() {

    init {
        fetch()
    }

    override fun createInitialState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnRefresh -> fetch()
            is Event.OnModalDismiss -> dismissModal()
        }
    }

    private fun setModal(modalState: ModalState) {
        setState { copy(modalState = modalState) }
    }

    private fun fetch() {
        // fetchDataUseCase 를 활용한 구현
    }

    private fun dismissModal() {
        setModal(ModalState.Dismiss)
    }
}
```

## 스크린 컴포저블에 대해

* 파일명 및 클래스는 {view}Screen.kt 로 명명합니다. `ex. NewsScreen.kt`
    * 모달의 경우 {view}BottomSheet.kt, {view}Dialog.kt 으로 명명합니다.
* 패키지 구조는 presentation.{view}.ui 로 합니다.
* Jetpack Compose 를 이용한 UI 개발을 담당하고, 뷰모델을 이용해 도메인과 소통합니다.
* 규약에 접근할 때는 import *.presentation.view.contract.ViewContract.State &
   Event & Effect 수준으로 임포트 하여 가독성을 높이길 권장합니다.
* 인자로 뷰모델을 가지며, 만약 뷰모델이 필요없는 단순한 구성일 경우 생략 가능합니다.
* 컨텐트 컴포저블로 State 를 활용한 UI 표현을 위임합니다.
* 이펙트를 관찰하며 사이드이펙트를 핸들링 합니다.
* ModalState 가 존재한다면 그에 따른 모달 상태도 핸들링 합니다.

### 컨텐트 컴포저블에 대해

* 일반적으로 스크린 컴포저블과 같은 파일 내에 정의됩니다.
* 스크린 컴포저블이 가진 뷰모델 의존성을 제외하고 State 를 활용한 UI 표현에만 집중해 프리뷰 기능 및 재사용성을 갖춘 영역입니다.
* 필수 인자는 state : State, emitEvent : (Event) -> Unit 입니다.
* Scaffold 를 베이스로 구현합니다.

```kotlin
// example
// package *.presentation.view.ui
//
// import *.presentation.view.contract.ViewContract.State   권장
// import *.presentation.view.contract.ViewContract.Event   권장
// import *.presentation.view.contract.ViewContract.Effect  권장
// import *.presentation.view.contract.ViewContract.ModalState 권장
// import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun ViewScreen(
    viewModel: ViewViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val emitEvent: (Event) -> Unit = viewModel::setEvent
    val launcher = rememberActivityResultLauncher() {}

    // UI 표현 위임
    ViewScreenContent(
        state = state,
        emitEvent = emitEvent
    )

    // 사이드이펙트 핸들링
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is Effect.Launch -> launcher.launch(effect.route)
            }
        }
    }

    // 모달 상태 핸들링
    when (val modalState = state.modalState) {
        is ModalState.Nont -> Unit
    }
}

@Composable
fun ViewScreenContent(
    state: State,
    emitEvent: (Event) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("View Title") })
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            // todo 컨텐츠 구현
        }
    }
}
```