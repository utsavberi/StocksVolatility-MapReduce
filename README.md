# StocksVolatility-MapReduce
Used Mapreduce on a Hadoop environment at CCR to compute the monthly volatility of stocks

##Calculate Stock Volatility of Monthly Rate of Return:

    xi = Monthly Rate of Return = (Month end adjusted close price – Month beginning adjusted close price) / (Monthly beginning adjusted close price)

    volatility =sqrt((1/n)(summ(xi-xb)^2)), xb = (1/n)summ(xi)

where N is set to 36 (months), ending close price is the close price of the last trading day in each month, beginning close price is the close price of the first trading day in each month.

Find
<ul>
<li> the top 10 stocks with the lowest(min) volatility
<li> the top 10 stocks with the highest(max) volatility
</ul>

##Data Format
For example, in the file AAPL.csv(Apple inc. stock), there are 7 columns, each row represents one day. The 5th column Close can be neglected(no use). 

<strong>Date</strong> represents the date of the stock AAPL;

<strong>Open</strong> represents the open price in that day of stock AAPL;

<strong>High</strong> represents the highest price in that day of stock AAPL;

<strong>Low</strong> represents the lowest price in that day of stock AAPL;

<strong>Adj Close</strong> represents the close price in that day of stock AAPL;

<strong>Volume</strong> represents the volume in that day of stock AAPL;


    <strong>Date Open High Low Close Volume Adj Close</strong>
    12/31/2014 112.82 113.13 110.21 110.38 41403400 110.38
    12/30/2014 113.64 113.92 112.11 112.52 29881500 112.52
    12/29/2014 113.79 114.77 113.7 113.91 27598900 113.91
    12/26/2014 112.1 114.52 112.01 113.99 33721000 113.99
    12/24/2014 112.58 112.71 112.01 112.01 14479600 112.01
