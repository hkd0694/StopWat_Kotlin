# Kotlin Study (2/9) - 2019/10/30

Kotlin을 공부하기 위해 간단한 앱부터 복잡한 앱까지 만들어 봄으로써 Kotlin에 대해 하기!

총 9개의 앱 중 두 번째 앱

프로젝트명 : StopWatch

기능

* 타이머를 시작, 일시정지하고 초기화할 수 있다.
  
* 타이머 실핼 중에 랩타임을 특정하여 표시한다.

핵심 구성요소

* Timer : 일정 시간 간격으로 코드를 백그라운드 스레드에서 실행한다.
  
* RunOnUiThread : 메인 스레드에서 UI를 갱신한다.
  
* ScrollView : 랩타임을 표시할 때 상하로 스크롤되는 뷰를 사용한다.

* FloatingActionButton : 머티리얼 디자인의 둥근 모양의 버튼이다.

라이브러리 설명

* `백터 드로어블 하위 호환 설정` : 안드로이드 5.0 미만에서 벡터 드로어블을 지원하는 라이브러리
  
* design 라이브러리 : FloatingActionButton 등 머티리얼 디자인을 제공하는 라이브러리


# timer, runOnUiThread

코틀린에서 일정한 시간을 주기로 반복하는 동작을 수행할 때는 `timer`기능을 사용한다. 

```Kotlin
    timer(period = 1000){
        //수행할 동작
    }
```
위의 코드는 1초 간격으로 어떤 동작을 수행하겠다는 의미이다.

하지만 이러한 timer 같은 오래 걸리는 작업은 메인 스레드가 아닌 보이지 않는 곳에서 오래 걸리는 작업을 수행하는 워커 스레드에서 작업을 하게 된다. 하지만 이 워커 스레드에서는 UI를 직접 조작을 할 수가 없다. 이러한 경우에는 runOnUiThread() 메서드를 사용한다.

```kotlin

private fun start(){
        fab.setImageResource(R.drawable.ic_pause_black_24dp)
        //timer는 워커 스레드에서 동작하기 땨문에 UI 동작이 불가능합니다.
        timerTask = timer(period = 10) {
            time++
            val sec = time / 100
            val milli = time % 100
            //그렇기 때문에 runOnUiThread로 감싸서 UI 조작이 가능하게 해줄 수 있습니다.
            runOnUiThread {
                secTextView.text = "$sec"
                milliTextView.text = "$milli"
            }
        }
    }

```

위의 코드에서 secTextView와 milliTextView는 activity_main.xml에 각각 TextView에 있는 메인 스레드에서 관리하는 ID 값들이다. 

하지만 timerTask = timer(period = 10) 블록 안은 메인스레드가 아닌 워커 스레드에서 작업을 하기 때문에 직접적으로 메인 스레드에서 관리하고 있는 textView ID에 접근을 하기 위해서는 runOnUiTHread()로 감싸주면 UI 조작이 가능하지게 된다.


## StopWat_Kotlin을 통해 배운 것들

### 동적 View 추가

LinearLayout에 동적으로 뷰를 추가하는 방법은 addView() 메서드를 사용한다.

```kotlin
    private fun recordLapTime(){
        val labTime = this.time;
        val textView = TextView(this)
        textView.text = "$lab LAP : ${labTime / 100}.${labTime % 100}"
        lapLayout.addView(textView,0)
        lab++
    }
```

위 코드에서 lapLayout.addView(textView,0)은 항상 맨 위(0)에 텍스트 뷰를 추가한다는 코드이다.

### ? 기호와 안전한 호출(?.) 연산자

> ### null 값을 허용할 땐 (?) 기호

기본적으로 코틀린은 null 값을 허용하지 않는다. 따라서 모든 객체는 생성과 동시에 값을 대입하여 초기화를 진행해야 한다.

```kotlin

    val a : String //(x) 초기화를 안했기 때문에 에러

    val a : String = null // (x) 코틀린은 기본적으로 null값을 허용하지 않음.

    val a : String? = null // (o) 사용 가능
```

코틀린에서 null값을 허용하려면 오른쪽에 ? 기호를 붙여줘야 한다.

```kotlin
    private var timerTask: Timer? = null
```

>### 안전한 호출(?.) 연산자

메서드 호출 시 점(.) 연산자 대신 ?. 연산자를 사용하게 되면 null값이 아닌 경우에만 호출된다. 

```kotlin
    val str: String? = null

    var upperCase = if(str != null) str else null // null
    upperCase = str?.toUpperCase // null
```

위의 코드는 str 변수의 값이 null 값이 아니라면 대문자로 변경하고, null 값이라면 null을 반환한다는 뜻이다.

```kotlin
    timerTask?.cancel()
```

위의 코드는 StopWat_Kotlin에서 나온 코드로 timerTask가 만약 null값이 아니라면 timer를 취소하겠다는 함수이다.


### 문자열 템플릿

코틀린에서 제공되는 문자열 템플릿 기능은 복잡한 문자열을 표현할 때 아주 편리하다. Java와 같이 '+' 기호로 문자열을 연결할 수 있고 $ 기호를 사용하면 문자열 리터럴 내부에 변수를 쉽게 포함 할 수 있다.

```kotlin
    val str = "Hello"

    println("$str 하세요")  //안녕 하세요
    println("${str}하세요") //안녕하세요

```

$옆에 {}를 붙이게 되면 변수와 글자를 붙여 쓸 수 있습니다.