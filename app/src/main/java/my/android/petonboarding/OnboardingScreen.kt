
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import my.android.petonboarding.OnBoardingItems

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
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopSection(
            onBackClick = {
                if (pagerState.currentPage > 0) scope.launch {
                    pagerState.scrollToPage(pagerState.currentPage - 1)
                }
            },
            onSkipClick = {
                if (pagerState.currentPage < items.size - 1) scope.launch {
                    pagerState.scrollToPage(items.size - 1)
                }
            }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            OnBoardingItem(items[page])
        }

        BottomSection(size = items.size, index = pagerState.currentPage) {
            if (pagerState.currentPage < items.size - 1) scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }
}

@Composable
fun TopSection(onBackClick: () -> Unit, onSkipClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth() // Changed from fillMaxSize to fillMaxWidth
            .background(Color.Black)
            .padding( 16.dp)
    ) {
        // Back button
        IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowLeft, tint = Color.White,
                contentDescription = "Back", modifier = Modifier.size(25.dp)
            )
        }

        // Skip button
        TextButton(onClick = onSkipClick, modifier = Modifier.align(Alignment.CenterEnd)) {
            Text(text = "Skip", color = Color.White, fontSize = 16.sp)
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
            // Indicators
            Indicators(size, index)

            // FAB
            FloatingActionButton(
                onClick = content,
                containerColor = Color.White,
                modifier = Modifier
                    .size(56.dp)
                    .offset(y = (-8).dp)
                    .clip(RoundedCornerShape(40.dp))
            ) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowRight,
                    tint = Color.Black,
                    contentDescription = "Next"
                )
            }
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
    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                if (isSelected) Color.White
                else Color.LightGray
            )
    )
}

@Composable
fun OnBoardingItem(item: OnBoardingItems) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
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
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
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