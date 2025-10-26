# MVIViewModel

## 상태(UiState) 에 대해
* UI 의 현재 상태를 나타내는 객체
* 상태는 VM 내에서만 업데이트 할 수 있고 `setState(reduce : State.() -> State)` 를 통해서 합니다.
* VM 내에서 현재 상태에 접근할 때는 `currentState` 로 접근 합니다.
* VM 내에서 `uiState.map{...}.distinctUntilChanged()` 의 형태로 다른 플로우와 결합해 트리거를 만들 수 있습니다.
* UI(Composable)에서 `by viewModel.uiState.collectAsStateWithLifecycle()` 의 형태로 상태를 구독하고 화면을 그립니다.
* 이 때 상태는 UI를 결정하는 유일한 데이터 여야 하며, 불변성을 유지해야 합니다. (recomposition 인지)

## 이벤트(UiEvent) 에 대해
* MVI 에 Intent 에 해당하는 사용자의 행동(의도).
* 이벤트는 사용자로부터 발생하기에 UI(Composable)로부터 주입되고 `viewModel.setEvent(event)` 를 통해서 합니다.
* VM 내에서는 `abstract fun handleEvent(event)` 를 구현하고 사용자로부터 발생한 이벤트를 핸들링 합니다.
* `handleEvent(event)` 는 내부에서 when 을 사용해 분기하며, 각 분기는 각각 독립된 함수로 구성한다.

## 이펙트(UiEffect) 에 대해
* UI의 상태와 무관한 화면 전환, 토스트, 스낵바 등 휘발성 효과.
* 이펙트는 VM 내에서만 업데이트 할 수 있고 `setEffect(effect)` 를 통해서 합니다.
* UI(Composable)에서 `viewModel.collectLatest` 를 통해 구독합니다. 
* 이펙트는 채널로 관리되고 있기 때문에 반드시 하나의 수신자(ex. collect)만 구독해야 합니다.
* collect 는 suspend 함수 이기 때문에 LaunchedEffect 내부에서 구독합니다.
* collectLatest 를 쓰는 이유는 연속된 이벤트가 발생할 때 각각의 이벤트에 대한 처리가 다음 이벤트를 지연시켜서 안되기 때문입니다.
* 만약 토스트 등 지연에 대한 처리를 명확히 하고 싶다면 각각의 처리에 더 구체적인 핸들링이 필요합니다.