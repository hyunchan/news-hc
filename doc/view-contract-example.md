# 화면 규약(View-Contract) 가이드

* {view} 는 작성하고자 하는 화면의 이름으로 최대한 직관적이되 중복을 피하는 것이 좋습니다.
* 화면 규약은 일반적으로 object 로 작성하며 {view}Contract 로 명명합니다.
```kotlin
// 로그인 관련 다양항 화면 규약 샘플
object LoginContract {}
object SettingLoginContract{}
object SsoLoginContract {}
```
* 패키지 구조는 `{namespace}.presentation.{view}.contract` 입니다.
* 필수 하위 규약은 상태 규약, 이벤트 규약, 이펙트 규약으로 반드시 구현해야 합니다.
* 하위 규약을 통하여 화면 규약에 의해 화면과 연결되는 뷰모델(MVIViewModel) 을 구현할 수 있습니다.
* 모달 상태 규약 등의 추가 하위 규약을 추가할 수 있습니다.

## 화면 규약 샘플

> AI 는 이 샘플에 따라 화면 규약을 생성하거나 수정 합니다.
```kotlin
// import com.hcpark.news.presentation.component.UiEffect
// import com.hcpark.news.presentation.component.UiEvent
// import com.hcpark.news.presentation.component.UiState

object ViewContract {
    // 상태 규약
    data class State(
        val modalState: ModalState = ModalState.Dismiss,
    ) : UiState

    // 이벤트 규약
    sealed class Event : UiEvent {
        data object OnRefreshRequested : Event()
        data object OnModalDismissRequested : Event()
    }

    // 이펙트 규약
    sealed class Effect : UiEffect {
        data class Launch(val route: String) : Effect()
    }

    // 모달 상태 규약 (추가 하위 규약)
    sealed class ModalState {
        data object None : ModalState()
        data class ErrorDialog(val message : String) : ModalState()
    }
}
```

# 뷰모델(ViewModel, VM, vm) 가이드

* 뷰모델은 일반적으로 MVIViewModel 을 상속하여 구현하고, 제네릭으로 필수 하위 규약을 주입합니다.
* 뷰모델 클래스의 이름은 {view}ViewModel 로 명명합니다.
* 패키지 구조는 {namespace}.presentation.{view}.viewmodel 로 합니다.
* 도메인과 뷰의 상호작용을 담당합니다.
* 비동기 연산 처리를 동기화 하여 안정적으로 뷰로 전달해야하기에 여기에 코루틴을 비롯한 비동기 처리 구현이 집중 됩니다.
* 화면 규약과 뷰모델은 일반적으로 1:1 관계입니다. 하지만 복잡도가 높은 화면은 구역을 분할해서 여러 화면 규약을 쓰기 떄문에 화면과 화면 규약, 뷰모델은 1:N:N 관계입니다.
* init 블록이 필요하다면 최상위에 구현합니다. 이후 오버라이드 함수를 구현합니다.
* `override fun handleEvent` 는 사용자 이벤트(의도)에 대한 처리를 구현합니다.
* 이벤트가 실행하는 함수는 이벤트의 이름을 반영하는 것이 아닌 함수 기능을 표현해야 합니다.
* Event.OnRefreshRequested 가 실행하는 함수는 `fun onRefresh(), fun refresh()` 보다는 `fun fetch()` 가 더 적절한 이름입니다.

## 뷰모델 샘플

> AI는 이 샘플에 따라 앞서 만든 화면 규약을 제네릭 상속하는 뷰모델을 생성하거나 수정 합니다.
```kotlin
// import {namespace}.presentation.component.MVIViewModel
// import {namespace}.presentation.{view}.contract.ViewContract.State
// import {namespace}.presentation.{view}.contract.ViewContract.Event
// import {namespace}.presentation.{view}.contract.ViewContract.Effect
// import {namespace}.presentation.{view}.contract.ViewContract.ModalState

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
            is Event.OnRefreshRequested -> fetch()
            is Event.OnModalDismissRequested -> dismissModal()
        }
    }

    private fun fetch() {
        // fetchDataUseCase 를 활용한 구현
    }

    private fun dismissModal() {
        setState { copy(modalState = ModalState.Dismiss) }
    }
}
```

# 스크린(Composable) 가이드

* 스크린은 화면 규약에 의해 그려질 컴포저블 함수를 뜻합니다.
* 함수명은 fun {view}Screen 로 명명합니다.
* 패키지 구조는 {namespace}.presentation.{view}.ui 로 합니다.
* 스크린의 필수 구현은 UI 와 이펙트(Side-Effect)의 관찰입니다.
* 만약 ModalState 같은 추가 규약이 있다면 그에 따른 관찰 및 핸들링도 구현 합니다.
* UI 는 Jetpack Compose 를 활용해 구현합니다.
* 역할 분리를 위해 Content 컴포저블로 UI 구현을 위임하기도 합니다. (권장)
* 이펙트는 뷰모델 내에 채널로 관리되고 있기에 반드시 단일 소비자 원칙을 준수해야 합니다.

## 컨텐트 컴포저블 가이드

* 스크린으로 부터 UI 구현을 역할을 위임 받은 컴포저블 함수입니다.
* 뷰모델 의존성을 가지지 않고, 상태 및 이벤트(사용자 행동, 인터렉션) 의 수집에 집중합니다.
* 이는 프리뷰 및 테스터에 친화적입니다.
* 스크린 컴포저블이 가진 뷰모델 의존성을 제외하고 State 를 활용한 UI 표현에만 집중해 프리뷰 기능 및 재사용성을 갖춘 영역입니다.

## 스크린, 컨텐트 컴포저블 샘플
```kotlin
// import {namespace}.presentation.view.contract.ViewContract.State
// import {namespace}.presentation.view.contract.ViewContract.Event
// import {namespace}.presentation.view.contract.ViewContract.Effect
// import {namespace}.presentation.view.contract.ViewContract.ModalState

@Composable
fun ViewScreen(
    viewModel: ViewViewModel
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val emitEvent: (Event) -> Unit = viewModel::setEvent
    val launcher = rememberActivityResultLauncher() {}

    // UI 표현 위임
    ViewScreenContent(
        state = state,
        emitEvent = emitEvent
    )

    // 이펙트 핸들링
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is Effect.Launch -> launcher.launch(effect.route)
            }
        }
    }

    // 모달 상태 핸들링
    when (val modalState = state.modalState) {
        is ModalState.None -> Unit
        is ModalState.ErrorDialog -> {
            ErrorDialog(
                message = modalState.message,
                onDismissRequest = { emitEvent(Event.OnModalDismissRequested) }
            )
        }
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
            // 바디 구현
        }
    }
}
```
