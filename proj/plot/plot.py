import happybase
import pandas as pd

import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation

import numpy as np
import matplotlib.units as munits
import matplotlib.dates as mdates

import datetime
from matplotlib.dates import DateFormatter
import matplotlib as mpl
# converter = mdates.ConciseDateConverter()
# munits.registry[np.datetime64] = converter
# munits.registry[datetime.date] = converter
# munits.registry[datetime.datetime] = converter


def connect(tname):
    CONNECTION = happybase.Connection('localhost', 9090)
    CONNECTION.open()
    # print(CONNECTION.tables())

    _table = CONNECTION.table(tname)
    print(_table)
    return _table


table = connect('pilot')

metas = pd.DataFrame({'col':[
                            b'country:US', 
                            b'country:CN',
                            b'country:JP',
                            b'country:ES',
                            b'country:IR',
                            b'country:IT'
                        ], 
                    'title':['United State', 'China', 'Japan', 'Spain', 'Iran', 'Italy'] } )


# def plot():

    # ax.xaxis.set_major_formatter(DateFormatter('%m-%d'))

    # lims = [(np.datetime64('2005-02'), np.datetime64('2005-04')),
    #     (np.datetime64('2005-02-03'), np.datetime64('2005-02-15')),
    #     (np.datetime64('2005-02-03 11:00'), np.datetime64('2005-02-04 13:20'))]


# locator = mdates.AutoDateLocator(minticks=3, maxticks=7)
# xfmt = mdates.DateFormatter('%Y-%m-%d')
# ax.xaxis.set_major_formatter(xfmt)
# ax.xaxis.set_major_locator(locator)
# ax.xaxis.set_major_formatter(formatter)

days = mdates.DayLocator()   # every date
months = mdates.MonthLocator()  # every month
fmt = mdates.DateFormatter('%m/%d')

    # plt.show()

def debug(col):
    print(col)
    # for i in col:
    #     print(i)


plt.style.use('fivethirtyeight')
# plt.legend(loc='upper left')

fig, ax = plt.subplots()

def setAxisTick(ax):

    ax.xaxis.set_major_locator(mdates.WeekdayLocator())
    ax.xaxis.set_major_formatter(mdates.DateFormatter('%b %d'))
    ax.yaxis.set_major_formatter(mpl.ticker.StrMethodFormatter('{x:,.0f}'))

    plt.title('Corona spread history')

    # ax.xaxis.set_major_locator(months)
    # ax.xaxis.set_major_formatter(fmt)
    # ax.xaxis.set_minor_locator(days)

    # ax.yaxis.set_major_formatter(mpl.ticker.StrMethodFormatter('{x:,.0f}'))

    # format the coords message box
    # ax.format_xdata = mdates.DateFormatter('%m-%d')
    # # ax.format_ydata = lambda x: '%1.2f' % x  # format the number.
    # ax.format_ydata = lambda x: '{:,}'.format(x)  # format the number.

def isZeroLast(col):
    colName = metas['col'].iloc[0]
    if colName in col:
        lastVal = col[colName].iloc[-1]
        print(lastVal)
        return lastVal == 0
    return False

def animate(i):
    # try:
    df = pd.DataFrame(table.scan(), columns=['date','countries'])
    df2 = pd.concat(
        [
            df.drop(['countries'], axis=1),
            df.countries.apply(pd.Series)
        ], axis=1)

    dates = pd.to_datetime(df2['date'].str.decode("utf-8"))
    debug(dates)
    # print('Last', dates[dates.index[-1]])
    if isZeroLast(df2)==True:
        return

    plt.cla()
    setAxisTick(ax)
    
    for (index, row) in metas.iterrows():
        colName = row['col']
        # print(row['col'], row['id'])

        if colName in df2:
            # print(colName)
            cases = df2[colName].str.decode("utf-8").fillna("0").apply(lambda x: int(x))
            debug(cases)
            ax.plot(dates, cases , label=row['title'] )
            
        
        # break
    
    plt.legend(loc='upper left')
    plt.tight_layout()
    # except:
    #     print("An exception occurred")

  # x_vals.append(next(index))
  # y_vals.append(random.randint(0,5))

  # plt.plot(x_vals, y_vals)

# plt.tight_layout()


ani = FuncAnimation(plt.gcf(), animate, interval=2500)
# animate(1)


fig.autofmt_xdate()
plt.tight_layout()

plt.show()
