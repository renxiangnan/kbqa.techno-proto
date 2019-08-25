package common;

public class MathUtils {

    public static int editDistance(String src, String dst) {
        int m = src.length();
        int n = dst.length();

        int[][] dp = new int[n + 1][m + 1];

        dp[0][0] = 0;
        for (int i = 1; i <= n; i ++) {
            dp[i][0] = i;
        }

        for (int j = 1; j <= m; j ++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i ++) {
            for (int j = 1; j <= m; j ++) {
                if (src.charAt(j - 1) == dst.charAt(i - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i][j - 1], Math.min(dp[i - 1][j], dp[i - 1][j - 1]));
                }
            }
        }

        return dp[n][m];
    }


}
