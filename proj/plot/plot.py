import happybase
import pandas as pd

import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation

import numpy as np
import matplotlib.units as munits
import matplotlib.dates as mdates

import datetime
# from matplotlib.dates import DateFormatter
# converter = mdates.ConciseDateConverter()
# munits.registry[np.datetime64] = converter
# munits.registry[datetime.date] = converter
# munits.registry[datetime.datetime] = converter


def connect(tname):
    CONNECTION = happybase.Connection('104.154.17.226', 9090)
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
    # formatter = mdates.ConcseDateFormatter(locator)
    # ax.xaxis.set_major_formatter(formatter)
    # for i in 
    # print(type(metas.col))
    
    # plt.show()

def debug(col):
    print(col)
    # for i in col:
    #     print(i)


plt.style.use('fivethirtyeight')
# plt.legend(loc='upper left')

# fig, ax = plt.subplots()

def animate(i):
    try:
        df = pd.DataFrame(table.scan(), columns=['date','countries'])
        df2 = pd.concat(
            [
                df.drop(['countries'], axis=1),
                df.countries.apply(pd.Series)
            ], axis=1)

        plt.cla()

        dates = pd.to_datetime(df2['date'].str.decode("utf-8"))
        # debug(dates)
        # ax.plot(dates, range(0,1000))
        # return 
        for (index, row) in metas.iterrows():
            colName = row['col']
            # print(row['col'], row['id'])

            if colName in df2:
                print(colName)
                cases = df2[colName].str.decode("utf-8").fillna("0").apply(lambda x: int(x))
                # debug(cases)
                plt.plot(dates, cases , label=row['title'] )
                # row['title']
            
            # break
        
        plt.legend(loc='upper left')
        plt.tight_layout()
    except:
        print("An exception occurred")

  # x_vals.append(next(index))
  # y_vals.append(random.randint(0,5))

  # plt.plot(x_vals, y_vals)

# plt.tight_layout()


ani = FuncAnimation(plt.gcf(), animate, interval=1000)
# animate(1)



plt.tight_layout()
plt.show()
