'''
Поляков А. И. М8О-308Б-19
Лабораторная работа №1.
Задание: Написать и отладить программу, строящую изображение заданной замечательной кривой.
Вариант №1
x^2/a^2 + y^2/b^2 = 1
'''
from bokeh.io import curdoc,output_notebook
from bokeh.layouts import row, column, widgetbox
from bokeh.models import ColumnDataSource, Column
from bokeh.models.widgets import TextInput, ColorPicker
from bokeh.plotting import Figure
from bokeh.palettes import Viridis, inferno
import numpy as np
from math import pi


def update_data(attr, old, new):
    try:
        x = float(a.value) * np.cos(t)
        y = float(b.value) * np.sin(t)
        source.data = dict(x=x, y=y)
    except ValueError:
        pass


t = np.linspace(0, 2 * pi, 100)
x = np.cos(t)
y = np.sin(t)

source = ColumnDataSource(data=dict(x=x, y=y))

plot = Figure(plot_width=700, x_range=(-10, 10), y_range=(-10, 10), aspect_ratio=1, tools="crosshair,pan,reset,save,wheel_zoom")
plot.xaxis.axis_label = "x"
plot.yaxis.axis_label = "y"
plot.line(x="x", y="y", source=source, line_width=2, legend_label="x^2/a^2 + y^2/b^2 = 1")
plot.title.text = "Plotting 2D-curves"

a = TextInput(title="a", value='1.0')
b = TextInput(title="b", value='1.0')

for w in [a, b]:
    w.on_change('value', update_data)


inputs = Column(a, b)

curdoc().add_root(row(inputs, plot, width=600))
curdoc().title = "CG lab1"
