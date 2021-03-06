package com.malkinfo.countdowntimer

import android.graphics.Color.green
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

const val TAG: String = "AppDebug"

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black)
            ){
                CountDownNumbers()
            }

        }
    }
}

@Composable
fun SetAnimation(){
    /**set animation*/
    var mcoloss:Color
    /**set Timer*/
    var animationState = remember{ mutableStateOf(true)}

    var setTime by remember{ mutableStateOf("")}
    var mintes:Long by remember{ mutableStateOf(10)}
    var sekend :Long by remember{ mutableStateOf(10)}
    val timCount = object :CountDownTimer(30000,1000){
        override fun onTick(mTimeLeftInMillis: Long) {
            mintes = (mTimeLeftInMillis/1000)/60
            sekend = (mTimeLeftInMillis/1000)%60
            setTime =  String.format(Locale.getDefault(),
                "%02d:%02d",
                mintes,sekend
            )

        }

        override fun onFinish() {
            animationState.value = false
            mcoloss = Color.Red
           if (sekend== 0L){
               animationState.value = false
               mcoloss = Color.Red
           }
        }

    }

    if (animationState.value){
        timCount.start()
        animationState.value = true
        mcoloss = Color.Red
    }
    else{
        timCount.cancel()
        animationState.value = false
        mcoloss = Color.Green
    }


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        Image(painter = painterResource(id = R.drawable.syk),
            contentDescription = "sky",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
       if (animationState.value){
           Rocket(
               isRocketEnabled = animationState.value,
               maxWidth = maxWidth,
               maxHeight = maxHeight
           )
       }
        LaunchButton(
            animationState = animationState.value,
            onToggleAnimationState = { timCount.cancel()
                setTime = "00:00"
                mcoloss = Color.Red
                animationState.value = !animationState.value
            }
        )
        Spacer(modifier = Modifier.height(35.dp))
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 120.dp
                ),
            horizontalArrangement = Arrangement.Center
        ){
            Text(text = setTime,
                color = mcoloss,
                modifier = Modifier
                    .padding( top = 15.dp,
                        start = 108.dp,
                        end = 108.dp,
                        bottom = 15.dp)
                    .fillMaxWidth(1f)
                    .border(2.dp, color = Color.White, RectangleShape)
                    .background(color = Color.Black, shape = RectangleShape)
                ,style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                fontSize = 50.sp
            )

        }
    }
}


@Composable
fun Rocket(
    isRocketEnabled: Boolean,
    maxWidth: Dp,
    maxHeight: Dp
) {
    val resource: Painter
    val modifier: Modifier
    val rocketSize = 250.dp
    if(!isRocketEnabled){
        resource = painterResource(id = R.drawable.jetman1)
        modifier = Modifier.offset(
            y = maxHeight - rocketSize,
        )
    }
    else{
        val infiniteTransition = rememberInfiniteTransition()
        val engineState = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 500,
                    easing = LinearEasing
                )
            )
        )
        val xPositionState = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000,
                    easing = LinearEasing
                )
            )
        )
        if (engineState.value <= .5f) {
            resource = painterResource(id = R.drawable.jetman2)
        } else {
            resource = painterResource(id = R.drawable.jetman3)
        }
        modifier = Modifier.offset(
            x = (maxWidth - rocketSize) * xPositionState.value,
            y = (maxHeight - rocketSize) - (maxHeight - rocketSize) * xPositionState.value,
        )
    }
    Image(
        modifier = modifier
            .width(rocketSize)
            .height(rocketSize),
        painter = resource,
        contentDescription = "A Rocket",
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CountDownNumbers() {
    val resourcess : Painter
    val infiniteTransition = rememberInfiniteTransition()
    val engineStates = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue =1f ,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500,
                easing = LinearEasing
            )
        )
    )
    if (engineStates.value <=0.5){
        resourcess = painterResource(id = R.drawable.jetman2)
    }
    else{
        resourcess = painterResource(id = R.drawable.jetman3)
    }

    /**set Coundowns */
    var nums: Long by remember { mutableStateOf(10) }

    var setView: String by remember { mutableStateOf("") }
    val cuntNum = object : CountDownTimer(
        10000,
        1000
    ) {
        override fun onTick(millisUntilFinished: Long) {
            nums = millisUntilFinished / 1000
            setView = "$nums"

        }

        override fun onFinish() {
            setView = "Launch...."

        }

    }
    /**set visible*/
     var visible by remember{ mutableStateOf(false)}

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 200.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!visible) {
                Button(
                    onClick = {
                        cuntNum.start()
                        visible = !visible
                    },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(Color.White),
                    elevation = ButtonDefaults.elevation(25.dp)
                ) {
                    Text(
                        text = "Start CountDown Launch ...!!",
                        fontSize = 20.sp,
                        color = Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(35.dp))
                    Image(
                        modifier = Modifier
                            .width(360.dp)
                            .height(360.dp),
                        painter = resourcess, contentDescription = "Roack"
                    )

            }
            Spacer(modifier = Modifier.height(16.dp))
           if(visible) {
               AnimationSS(mag = setView)
           }
            val shape = CircleShape
        }

        if (setView == "Launch....") {
            cuntNum.cancel()
            SetAnimation()
            if(!visible) {
                AnimationSS(mag = setView)
            }
        }


}




@Composable
fun LaunchButton(
    animationState: Boolean,
    onToggleAnimationState: () -> Unit,
){
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 50.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
            horizontalArrangement = Arrangement.Center
        ) {
            if (animationState) {
                Button(modifier = Modifier
                    .padding(top = 16.dp)
                    .border(2.dp, Color.Red, CircleShape)
                    ,
                    onClick = onToggleAnimationState,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Red
                    ),
                    elevation = ButtonDefaults.elevation(25.dp),
                    shape = CircleShape
                ) {
                    Text("STOP",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        fontSize = 25.sp,
                        color = Color.Red
                    )
                }
            } else {
                Button(modifier = Modifier
                    .padding(top = 16.dp)
                    .border(2.dp, Color.Green, CircleShape)
                    ,
                    onClick = onToggleAnimationState,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.White,
                        contentColor = Color.Green
                    ),
                    elevation = ButtonDefaults.elevation(25.dp),
                    shape = CircleShape
                ) {
                    Text("Start Fly...",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        fontSize = 25.sp,
                        color = Color.Green
                    )
                }
            }
        }
    }
}
enum class BoxState{
    Small,Large
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimationSS(mag:String){
    val colorss:Color

    val boxState by remember{ mutableStateOf(BoxState.Small)}
    val transition = updateTransition(targetState = boxState)
    val infiniteTransition = rememberInfiniteTransition()
    val poisitions = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )
    if (poisitions.value<= 0.5f){
        colorss = Color.Green
    }
    else{
        colorss = Color.Red
    }
    val pul = transition.animateFloat() { state ->
        when (state) {
            BoxState.Large -> poisitions.value * 350
            BoxState.Small -> poisitions.value * 180
        }
    }
    Card(
        modifier = Modifier
                   .padding(
                       bottom = 9.dp,
                       top = 80.dp,
                       start = 5.dp,
                       end = 5.dp
                   )
        ,
               shape = CircleShape,
               elevation = 100.dp,
        contentColor = colorss,
           ) {
               Box(
                   modifier = Modifier
                       .background(color = Color.White, shape = CircleShape)
                       .height(pul.value.dp)
                       .width(pul.value.dp)
                       .border(2.dp, color = colorss, shape = CircleShape),
                   contentAlignment = Alignment.Center,
               ) {
                   val shape = CircleShape
                   Text(
                       text = mag,
                       style = TextStyle(
                           fontWeight = FontWeight.Bold,
                           textAlign = TextAlign.Center
                       ),
                       modifier = Modifier
                           .padding(16.dp)
                           .padding(16.dp),
                       fontSize = 60.sp,
                       color = colorss,

                       )
               }
           }


    }





