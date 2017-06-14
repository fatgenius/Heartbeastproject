import sqlite3
import time
import datetime
import random
import matplotlib.pyplot as plt
import numpy as np
import sys

from scipy.fftpack import fft
from ABE_ADCDACPi import ADCDACPi

import Adafruit_GPIO.SPI as SPI
import Adafruit_SSD1306

from PIL import Image
from PIL import ImageDraw
from PIL import ImageFont

import subprocess




conn = sqlite3.connect(str(sys.argv[1]))
c = conn.cursor()
name =str(sys.argv[2])
def create_table():
    c.execute("CREATE TABLE IF NOT EXISTS testadc( name TEXT, datastamp TEXT, adc REAL)")
    c.execute("CREATE TABLE IF NOT EXISTS testbpm( name TEXT,startime TEXT, endtime TEXT, bpm REAL)")

def rad_adc():
    adc=ADCDACPi() 
    adc.set_adc_refvoltage(3.3)
    Signal = adc.read_adc_voltage(1, 0)
    return Signal

def dynamic_data_entry():
    for i in xrange(200):
        adc = rad_adc()
        my_list.append(adc)
       # time.sleep(0.004)
        unix = int(time.time())
        date = str(datetime.datetime.fromtimestamp(unix).strftime('%Y-%m-%d %H:%M:%S.%f'))
       # name = 'Python'
        c.execute("INSERT INTO testadc(name,datastamp,adc) VALUES (?, ?, ?)",
          (name,date, adc))
    conn.commit()

my_list=list()
create_table()
unix1=int(time.time())   
beginTime=str(datetime.datetime.fromtimestamp(unix1).strftime('%Y-%m-%d %H:%M:%S.%f'))


for i in range(10):   
    dynamic_data_entry()
    time.sleep(1)
   # send_database()
y=abs(fft(my_list))

for x in range(0,12):
    y[x]=0

for x in range(35,len(y)):
    y[x]=0
bpm=(np.argmax(y))*6
print(bpm)
#name='Python'
unix2=int(time.time())
endtime=str(datetime.datetime.fromtimestamp(unix2).strftime('%Y-%m-%d %H:%M:%S.%f'))
c.execute("INSERT INTO testbpm (name ,startime , endtime,bpm) VALUES (?, ?, ?, ?)",
          (name, beginTime,endtime, 100))    
conn.commit()
c.close
conn.close()

RST = None     # on the PiOLED this pin isnt used
DC = 23
SPI_PORT = 0
SPI_DEVICE = 0

disp = Adafruit_SSD1306.SSD1306_128_32(rst=RST)

disp.begin()

# Clear display.
disp.clear()
disp.display()

# Create blank image for drawing.
# Make sure to create image with mode '1' for 1-bit color.
width = disp.width
height = disp.height
image = Image.new('1', (width, height))

# Get drawing object to draw on image.
draw = ImageDraw.Draw(image)

# Draw a black filled box to clear the image.
draw.rectangle((0,0,width,height), outline=0, fill=0)

# Draw some shapes.
# First define some constants to allow easy resizing of shapes.
padding = -2
top = padding
bottom = height-padding
# Move left to right keeping track of the current x position for drawing shapes.
x = 0
font = ImageFont.load_default()
a=bpm


    # Draw a black filled box to clear the image.
draw.rectangle((0,0,width,height), outline=0, fill=0)

    # Shell scripts for system monitoring from here : https://unix.stackexchange.com/questions/119126/command-to-display-memory-usage-disk-usage-and-cpu-load
cmd = "hostname -I | cut -d\' \' -f1"
IP = subprocess.check_output(cmd, shell = True )
cmd = "top -bn1 | grep load | awk '{printf \"CPU Load: %.2f\", $(NF-2)}'"
CPU = subprocess.check_output(cmd, shell = True )
cmd = "free -m | awk 'NR==2{printf \"Mem: %s/%sMB %.2f%%\", $3,$2,$3*100/$2 }'"
MemUsage = subprocess.check_output(cmd, shell = True )
cmd = "df -h | awk '$NF==\"/\"{printf \"Disk: %d/%dGB %s\", $3,$2,$5}'"
Disk = subprocess.check_output(cmd, shell = True )

    # Write two lines of text.
    
draw.text((x, top),       "NAME is: "+str(name),  font=font, fill=255)
draw.text((x, top+8),    "BPM is: "+ str(a), font=font, fill=300)
draw.text((x, top+16),   "TIME is: " +str(beginTime),  font=font, fill=255)
   # draw.text((x, top+25),    str(Disk),  font=font, fill=255)

    # Display image.
disp.image(image)
disp.display()
time.sleep(.1)
    
