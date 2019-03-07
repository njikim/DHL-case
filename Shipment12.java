//  Zhenzheng Jia I6100566     Eunji Kim I6073884        Aline Fattal I6093879
import java.util.*;
import java.io.*;
public class Shipment12
{   public int dummy_airline;
    public int id;
    public int haworg_nbre;  
    public int dateofshipment;
    public int destination_nbre;  
    public int time_of_shipment;
    public double avweight;
    public int date_and_timedueofshipment;
// function to check if the shipment is via airline or not. 
public boolean check_shipmenttype()  
{ if(dummy_airline==0)
   { return false; // the shipment is by airline
   }
  return true;
}  
// printing function for the shipments
 /* public void printing_function_ofshipments(Shipment12[] shipment,int i)
 {
    System.out.println(shipment[i].dummy_airline+ " " + shipment[i].id+" "+ shipment[i].haworg_nbre+" "+ shipment[i].dateofshipment+" "+ shipment[i].destination_nbre+" "+ shipment[i].time_of_shipment+" "+ shipment[i].avweight+" "+shipment[i].date_and_timedueofshipment);
 }*/
 /* public void printing_ofshipements_airline( Shipment12[] shipment111,int i) 
  {
      System.out.println(shipment111[i].dummy_airline+ " " + shipment111[i].id);
  }*/
}