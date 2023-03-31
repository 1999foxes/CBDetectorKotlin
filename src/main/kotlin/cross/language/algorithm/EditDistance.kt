package cross.language.algorithm


fun editDistance(s1: String, s2: String): Int {
    val dis = MutableList<MutableList<Int>>(s1.length + 1) {
        MutableList<Int>(s2.length + 1) { 0 }
    }
    for (i in 0..s1.length)
        dis[i][0] = i
    for (j in 0..s2.length)
        dis[0][j] = j

    for (j in 1..s2.length) {
        for (i in 1..s1.length) {
            dis[i][j] = listOf(
                dis[i - 1][j] + 1,
                dis[i][j - 1] + 1,
                dis[i - 1][j - 1] + (s1[i - 1] != s2[j - 1]).toInt(),
            ).min();
        }
    }
    return dis[s1.length][s2.length];
}

fun Boolean.toInt() = if (this) 1 else 0