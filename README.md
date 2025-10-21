# Vertical Line Counter

The approach that I've taken is that, countVerticalBlackLines function scans each column in the image, counts how many pixels are black, and counts only spots where a big group of black pixels means there’s a vertical line. I've annotated in depth in the code. I've also created a simple user interface to browse and select multiple images at once. We just need to run VerticalLineCounterGUI just like any other java file. it will open the Graphical User Interface, then browse and select single/multiple images. It will automatically display the number of vertical lines present in that image. 


**#How to run this program**

step 1 : git clone https://github.com/pardha18145/vertical-line-counter.git
step 2 : checkout main branch
step 3 : run VerticalLineCounterGUI from any IDE.

or 
          
          Alternately, you can use java console commands

          javac com/example/verticalcounter/ui/VerticalLineCounterGUI.java \
          com/example/verticalcounter/model/ImageResult.java \
          com/example/verticalcounter/renderer/ImageResultCellRenderer.java \
          com/example/verticalcounter/service/VerticalLineCounter.java \
          -d ../out

          and 

          java -cp out com.example.verticalcounter.ui.VerticalLineCounterGUI

