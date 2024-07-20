package my.android.petonboarding

data class OnBoardingItems(
    val title: Int, val image: Int, val desc : Int
){
    companion object
    {
        fun get() = listOf(
            OnBoardingItems(R.string.title1, R.drawable.image1, R.string.desc1),
            OnBoardingItems(R.string.title2, R.drawable.image2, R.string.desc2),
            OnBoardingItems(R.string.title3, R.drawable.image3, R.string.desc3)
        )
    }
}