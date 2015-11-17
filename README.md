# StocksVolatility-MapReduce
Used Mapreduce on a Hadoop environment at CCR to compute the monthly volatility of stocks

Calculate Stock Volatility of Monthly Rate of Return:
xi = Monthly Rate of Return = (Month end adjusted close price – Month beginning adjusted
close price) / (Monthly beginning adjusted close price)
volatility =sqrt((1/n)(summ(xi-xb)^2)), xb = (1/n)summ(xi)
 , where N is set to 36 (months), ending
close price is the close price of the last trading day in each month, beginning close
price is the close price of the first trading day in each month.

Find
<ul>
<li> the top 10 stocks with the lowest(min) volatility
<li> the top 10 stocks with the highest(max) volatility
