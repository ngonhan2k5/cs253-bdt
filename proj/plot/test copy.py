import happybase
import pandas as pd

import matplotlib.pyplot as plt
from matplotlib.animation import FuncAnimation

import numpy as np
import matplotlib.units as munits
import matplotlib.dates as mdates

import datetime
from matplotlib.dates import DateFormatter
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

xs = []
ys = []


# for (key, data) in table.scan(limit=10):
#     print(key, data)

# d = [(a, b, c) for a, (b, c) in l]
df = pd.DataFrame(table.scan(), columns=['date','countries'])
dd = df.countries.apply(pd.Series)
# print("aaa", dd)

metas = pd.DataFrame({'col':[
                            b'country:US', 
                            b'country:CN',
                            b'country:JP',
                            b'country:UK',
                            b'country:KR',
                        ], 
                    'title':['US', 'CN', 'JP', 'UK', 'KR'] } )

df2 = pd.concat(
        [
            df.drop(['countries'], axis=1),
            df.countries.apply(pd.Series)
        ], axis=1)

print(df2)

def test():
    df["date"] = pd.to_datetime(df["date"].str.decode("utf-8"))
    print(df)


# # for i in metas.columns[1:]:
# # for name, values in df.iteritems():

def plot():
    plt.style.use('fivethirtyeight')

    fig, ax = plt.subplots()

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
    for (index, row) in metas.iterrows():
        colName = row['col']
        # print(row['col'], row['id'])
        if colName in df2:
            print(colName)
            # print(df2[colName])
            # plt.plot(df2['date'].apply(lambda x:x[4:6]+b'/'+x[6:8]) , df2[colName].fillna("0"), label=row['title'])
            # print("----", colName)
            # print( df2['date'], df2[colName].apply(lambda x:), row['title'])
            # ax.plot(b'date', b'country:CN', data=df2)
            cases = df2[colName].str.decode("utf-8").fillna("0").apply(lambda x: int(x))
            debug(cases)
            cases = np.cumsum(cases)
            debug(cases)
            # print(cases)
            # for i in cases:
            #     print(type(i))
            # int.from_bytes(b'y\xcc\xa6\xbb', byteorder='big')
            dates = pd.to_datetime(df2['date'].str.decode("utf-8"))
            # dates = [datetime.datetime.strptime(d, '%Y-%m-%d') for d in dates]
            
            # int.from_bytes(b'y\xcc\xa6\xbb', byteorder='big')
            ax.plot(dates, cases , label=row['title'] )
        
        # break


    plt.legend(loc='upper left')
    plt.tight_layout()
    plt.show()

# for column in metas:
#     print(metas[column])
    # for i in metas[column]
    #     print(i)

# for name, values in metas.iteritems():
#     print('{name}: {value}'.format(name=name, value=values))




def debug(col):
    print(col)
    for i in col:
        print(i)

# for p in meta.index

# plt.legend(loc='upper left')
# plt.tight_layout()


plot()