/**
 * EvaluationResultGUI.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 07/05/2007
 */
package com.demo.jcolibri.evaluation.tools;

import jcolibri.evaluation.EvaluationReport;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;



/**
 * Class that visualizates the result of an evaluation in a Swing frame.
 * It generates a chart with the evaluation result an other information returned by the evaluator.
 * 
 * @author Juan A. Recio-Garcia
 *
 */
public class EvaluationResultGUI
{
    private static final long serialVersionUID = 1L;
    
//    private static Chart2D chart;
    private static JFrame dialog;
    private static EvaluationReport evalReport; 
    
    public static void show(EvaluationReport er, String title, boolean exitOnClose)
    {
        evalReport = er;
        
       dialog = new JFrame();
       dialog.setTitle("jCOLIBRI Evaluation");
       dialog.getContentPane().setLayout(new BorderLayout());
       
       JPanel data = new JPanel();
       data.setLayout(new BoxLayout(data,BoxLayout.X_AXIS));
       data.add(new JLabel("Cycles: "+ er.getNumberOfCycles()));
       data.add(Box.createGlue());
       data.add(new JLabel("Time: "+ er.getTotalTime()+" ms"));
       data.add(Box.createGlue());
       data.add(new JLabel("Time per cycle: "+ er.getTimePerCycle()+" ms"));
       //data.add(Box.createGlue());
       //data.add( new JLabel("Average: "+ String.format("%6f",er.getEvaluationAverage())));
       dialog.getContentPane().add(data,BorderLayout.NORTH);

//       chart = getChart(title, er);
       
       JTextArea textArea = new JTextArea();
       JScrollPane sp = new JScrollPane(textArea);
       sp.setViewportView(textArea);
       textArea.setText(er.toString());
       textArea.setEditable(false);
              
//       dialog.getContentPane().add(chart, BorderLayout.CENTER);
       
       JPanel buttons = new JPanel();
       //buttons.setLayout(new BoxLayout(buttons,BoxLayout.X_AXIS));
       JButton exportData = new JButton("Export data");
       exportData.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                try{
                    FileDialog fd = new FileDialog(dialog, "Save as CSV", FileDialog.SAVE);
                    fd.setFile("evaluation.csv");
                    fd.setVisible(true);
                    String name = fd.getDirectory() + fd.getFile();
                    File file = new File(name);
                    saveEvaluationToCSV(evalReport, file);
                }
                catch(Exception ex) { 
                    ex.printStackTrace(); 
                }                
            }
           
       });
       
       
       JButton exportChart = new JButton("Export chart");
       exportChart.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try{
                    FileDialog fd = new FileDialog(dialog, "Save as JPG", FileDialog.SAVE);
                    fd.setFile("evaluation.jpg");
                    fd.setVisible(true);
                    String name = fd.getDirectory() + fd.getFile();
                    File file = new File(name);
//                    saveComponentToJPG(chart, file);
                }
                catch(Exception ex) { 
                    ex.printStackTrace(); 
                }
            }
       });

       
       buttons.add(exportData);
       buttons.add(exportChart);
       
       JPanel p = new JPanel();
       p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
       p.add(sp);
       p.add(buttons);

       
       dialog.getContentPane().add(p, BorderLayout.SOUTH);

       /*
       dialog.addWindowListener(new WindowAdapter()
       {  public void windowClosing(WindowEvent we)
          {  System.exit(0);
          }
       });
       */
       dialog.setPreferredSize(new Dimension(640,400));
       dialog.pack();
       dialog.doLayout();
       dialog.setVisible(true);

       if(exitOnClose)
           dialog.addWindowListener(new WindowAdapter() 
           {   public void windowClosing(WindowEvent arg0) 
               {   System.exit(0);
               }
           });
    }
    
    
//    private static Chart2D getChart(String title, EvaluationReport er) {
//
//        //<-- Begin Chart2D configuration -->
//
//        //Configure object properties
//        Object2DProperties object2DProps = new Object2DProperties();
//        object2DProps.setObjectTitleText (title);
//
//        //Configure chart properties
//        Chart2DProperties chart2DProps = new Chart2DProperties();
//        chart2DProps.setChartDataLabelsPrecision (-2);
//
//        //Configure legend properties
//        LegendProperties legendProps = new LegendProperties();
//        legendProps.setLegendExistence(true);
//        String[] labels = er.getSeriesLabels();
//        legendProps.setLegendLabelsTexts (labels);
//
//        //Configure graph chart properties
//        GraphChart2DProperties graphChart2DProps = new GraphChart2DProperties();
//        //String[] labelsAxisLabels = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
//        graphChart2DProps.setLabelsAxisExistence(false); //setLabelsAxisLabelsTexts (labelsAxisLabels);
//        //graphChart2DProps.setLabelsAxisTitleText ("Iteration");
//        graphChart2DProps.setNumbersAxisTitleText ("Evaluation");
//        graphChart2DProps.setLabelsAxisTicksAlignment (GraphChart2DProperties.CENTERED);
//        //graphChart2DProps.setChartDatasetCustomGreatestValue(1);
//        //graphChart2DProps.setChartDatasetCustomizeGreatestValue(true);
//
//        //Configure graph properties
//        GraphProperties graphProps = new GraphProperties();
//        graphProps.setGraphBarsExistence (false);
//        graphProps.setGraphLinesExistence (true);
//        graphProps.setGraphLinesThicknessModel (2);
//        graphProps.setGraphLinesWithinCategoryOverlapRatio (1f);
//        graphProps.setGraphDotsExistence (false);
//        graphProps.setGraphDotsThicknessModel (4);
//        graphProps.setGraphDotsWithinCategoryOverlapRatio (1f);
//        graphProps.setGraphAllowComponentAlignment (true);
//        graphProps.setGraphOutlineComponentsExistence (true);
//
//        int lines = labels.length;
//        int lineSize = er.getSeries(labels[0]).size();
//
//        //Configure dataset
//        Dataset dataset = new Dataset (lines, lineSize, 1);
//
//        for( int l=0; l<lines; l++)
//        {
//            Vector<Double> line = er.getSeries(labels[l]);
//            for (int j = 0; j < dataset.getNumCats(); ++j) {
//                  dataset.set (l, j, 0, (float)line.get(j).floatValue());
//                }
//        }
//
//        //Configure graph component colors
//        MultiColorsProperties multiColorsProps = new MultiColorsProperties();
//
//        //Configure chart
//        LBChart2D chart2D = new LBChart2D();
//        chart2D.setObject2DProperties (object2DProps);
//        chart2D.setChart2DProperties (chart2DProps);
//        chart2D.setLegendProperties (legendProps);
//        chart2D.setGraphChart2DProperties (graphChart2DProps);
//        chart2D.addGraphProperties (graphProps);
//        chart2D.addDataset (dataset);
//        chart2D.addMultiColorsProperties (multiColorsProps);
//
//        //Optional validation:  Prints debug messages if invalid only.
//        if (!chart2D.validate (false)) chart2D.validate (true);
//
//        //<-- End Chart2D configuration -->
//
//        return chart2D;
//      }
    

     static void saveEvaluationToCSV(EvaluationReport er, File file) throws IOException{
         PrintWriter pw = new PrintWriter(file);
         pw.println("# Cycles: "+ er.getNumberOfCycles());
         pw.println("# Time: "+ er.getTotalTime()+" ms");
         pw.println("# Time per cycle: "+ er.getTimePerCycle()+" ms");
         //pw.println("# Average: "+ String.format("%6f",er.getEvaluationAverage()));
         
         String[] labels = er.getSeriesLabels();
         
         for(int l = 0; l<labels.length; l++)
         {
             Vector<Double> res = er.getSeries(labels[l]);
             pw.print(labels[l]);
             for(int i=0; i<res.size(); i++)
                 pw.print(";"+res.get(i));
             pw.println();
         }
             
         pw.close();
     }
          
     static void saveComponentToJPG(Component component, File file) throws IOException{
            BufferedImage image = (BufferedImage)component.createImage(component.getWidth(),component.getHeight());
            Graphics graphics = image.getGraphics();
            if(graphics != null) { component.paintAll(graphics); }
            FileOutputStream fileStream = new FileOutputStream(file);
//            JPEGEncodeParam encodeParam = JPEGCodec.getDefaultJPEGEncodeParam(image);
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fileStream);
//            encoder.encode(image,encodeParam);
            fileStream.close();
     }

     
}
