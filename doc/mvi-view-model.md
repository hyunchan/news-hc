# MVIViewModel
* MVIViewModel 의 구현을 위해 먼저 화면 규약(ViewContract) 를 준비 해야합니다.
* 준비된 화면 규약을 제네릭 상속 받아 구현합니다.
* 화면 규약은 MVI의 기본이 되는 3가지 규약의 집합입니다. (UiState:상태, UiEvent:이벤트, UiEffect:이펙트)

## 상태 규약에 대해
* 선언형 UI의 상태를 정의합니다.
* 상태는 지속성을 지니고 있어 변화는 이전 상태에 기반합니다.
* 업데이트는 **엄격하게** 뷰모델 내에서만 수행합니다!
```kotlin
// 이전 상태에 기반해 업데이트를 수행하기 위해 copy 를 활용합니다.
setState { copy(isLoading = true) }
```
* 상태의 스냅샷은 UI를 추론할 수 있는 직관적인 데이터입니다.
* 리컴포지션 최적화를 위해 상태의 멤버(요소)들은 **불변(Stable)** 해야합니다.
* 상태의 변화를 통해 특정 요소의 업데이트나 관찰의 변화가 요구될 때는 뷰모델 내부에서 트리거를 만들어 사용합니다.
```kotlin
uiState
    .map {
        // state 의 전체를 관찰하지 말고 변화를 감지할 요소만을 타겟해야함
        it.isLogin to it.userName   // 두가지 요소의 결합을 통해 트리거를 만듦
    }
    .distinctUntilChanged() // (optional) 상태가 업데이트 되었으나 값이 변경되지 않았을 경우 수행을 제한하기 위해
    .collect {
        // 수행
    }
```
* 컴포저블 함수에서 상태를 접근할 땐 생명주기에 따라 리소스를 낭비하지 않도록 합니다.
```kotlin
@Composable
fun ViewScreen(viewModel : ViewModel){
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    // UI 구현
}
```

## 이벤트 규약에 대해
* 사용자의 행동을 정의합니다. `MVI 에 Intent 에 해당`
* 이벤트는 sealed class 를 통해 정의합니다.
* 이벤트는 콜백(ex. onClick) 을 통해 수집되어 뷰모델로 전달합니다.
```kotlin
@Composable
fun ViewScreen(viewModel : ViewModel) {
    Button(
        onClick = { viewModel.setEvent(Event.OnClicked) }
    )
    ViewScreenTopBar(
        emitEvent = viewModel::setEvent
    )
}

// 또는 뷰모델 의존성을 제거한 순수 컴포저블 함수의 경우
@Composable
fun ViewScreenTopBar(
    emitEvent : (Event) -> Unit,
){
    emitEvent(Event.OnRefreshRequested)
}
```
* 뷰모델에서는 `handleEvent` 메소드를 오버라이드하여 로직을 처리합니다.
```kotlin
override fun handleEvent(event: Event) {
    when(event){
        Event.OnClicked -> showDialog()
    }
}
```

## 이펙트 규약에 대해
* 이펙트는 상태와 달리 일회성 사건입니다. (ex. 화면 이동, 토스트, 스낵바 등)
* 이펙트도 sealed class 를 통해 정의합니다.
* 상태는 이전 상태와 다음 상태가 연속된 전이를 통해 흐름을 만들지만 이펙트는 각기 독립적이고 서로 영향 및 전이가 없습니다.
* 이펙트도 엄격하게 뷰모델 내에서만 업데이트 합니다.
```kotlin
setEffect(Effect.ShowToast)
```
* 이펙트는 **Channel**로 구현되어 있습니다. 이는 **단일 소비자 원칙**을 의미하고, 중복 처리를 방지를 보장합니다.
* 소비는 LaunchedEffect 내에서 collect 통해 이뤄집니다.
```kotlin
fun ViewScreen(viewModel : ViewModel){
    LaunchedEffect(viewModel.effect){
        viewModel.effect.collect{
            when(it){
                Effect.ShowToast -> showToast()
            }
        }
    }
}
```
* 이펙트의 처리가 지연된다면 다음 이펙트의 처리가 지연될 수 있기 때문에 toast 같이 지연을 발생시키는 이펙트를 소비할 때는 주의해야 합니다!
