
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import my.android.petonboarding.OnBoardingItems
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun OnboardingScreen() {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val items = OnBoardingItems.get()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        TopSection(
            onBackClick = {
                if (pagerState.currentPage > 0) scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                }
            },
            onSkipClick = {
                if (pagerState.currentPage < items.size - 1) scope.launch {
                    pagerState.animateScrollToPage(items.size - 1)
                }
            }, pagerState = pagerState
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            AnimatedOnBoardingItem(items[page], page, pagerState.currentPage, pagerState.currentPageOffsetFraction)
        }

        BottomSection(size = items.size, index = pagerState.currentPage) {
            if (pagerState.currentPage < items.size - 1) scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopSection(onBackClick: () -> Unit, onSkipClick: () -> Unit, pagerState: PagerState) {
    val backButtonVisible by remember (pagerState.currentPage) {
        derivedStateOf{ pagerState.currentPage > 0} }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(16.dp)
            .padding(top = 10.dp)
    ) {
        AnimatedVisibility(visible = backButtonVisible) {

        IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                tint = Color.White,
                contentDescription = "Back",
                modifier = Modifier.size(25.dp)
            )
        }
    }
        TextButton(onClick = onSkipClick, modifier = Modifier.align(Alignment.CenterEnd)) {
            Text(text = "Skip", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun AnimatedOnBoardingItem(item: OnBoardingItems, page: Int, currentPage: Int, currentPageOffset: Float) {
    val pageOffset = (page - currentPage) + currentPageOffset
    val imageSize by animateDpAsState(
        targetValue = if (page == currentPage) 300.dp else 250.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                alpha = 1f - pageOffset.absoluteValue
                translationX = pageOffset * size.width * 0.5f
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Box(
                modifier = Modifier
                    .size(imageSize)
                    .background(Color.Black),
            ) {
                Image(
                    painter = painterResource(id = item.image),
                    contentDescription = "Onboarding image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = item.title),
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = item.desc),
                fontSize = 16.sp,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        }
    }
}

@Composable
fun BottomSection(size: Int, index: Int, content: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Indicators(size, index)
            AnimatedFAB(onClick = content)
        }
    }
}

@Composable
fun AnimatedFAB(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.8f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = ""
    )

    FloatingActionButton(
        onClick = {
            isPressed = true
            onClick()
        },
        containerColor = Color.White,
        modifier = Modifier
            .size(56.dp)
            .offset(y = (-8).dp)
            .clip(RoundedCornerShape(40.dp))
            .scale(scale)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            tint = Color.Black,
            contentDescription = "Next"
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}

@Composable
fun Indicators(size: Int, index: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        repeat(size) {
            Indicator(isSelected = it == index)
        }
    }
}

@Composable
fun Indicator(isSelected: Boolean) {
    val width by animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = ""
    )
    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width)
            .clip(CircleShape)
            .background(
                if (isSelected) Color.White
                else Color.LightGray
            )
    )
}