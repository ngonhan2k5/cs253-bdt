import happybase
import pandas as pd

import matplotlib.pyplot as plt

import numpy as np
import matplotlib.units as munits
import matplotlib.dates as mdates

import datetime
from matplotlib.dates import DateFormatter
import matplotlib as mpl

def connect(tname):
    CONNECTION = happybase.Connection('localhost', 9090)
    CONNECTION.open()
    # print(CONNECTION.tables())

    _table = CONNECTION.table(tname)
    return _table
 
def debug(col):
    print(col)
    # for i in col:
    #     print(i)

table = connect("cases_by_date")

df = pd.DataFrame(table.scan(), columns=['date','cases'])
df2 = pd.concat(
    [
        df.drop(['cases'], axis=1),
        df.cases.apply(pd.Series)
    ], axis=1)

dates = pd.to_datetime(df2['date'].str.decode("utf-8"))

fig, ax = plt.subplots()

fig.autofmt_xdate()
fig.set_size_inches(12, 8)

objects = df2[b'ana:confirmedCases'].apply(lambda x: int(x))
y_pos = np.arange(len(objects))


ax.bar(dates, objects, align='center', alpha=0.5)
# plt.xticks(y_pos, dates)

ax.xaxis.set_major_locator(mdates.WeekdayLocator())
ax.xaxis.set_major_formatter(mdates.DateFormatter('%b %d'))
ax.yaxis.set_major_formatter(mpl.ticker.StrMethodFormatter('{x:,.0f}'))

plt.style.use('fivethirtyeight')
plt.title('Confirm case by date of World')

plt.show()