
package com.ag2.presentation.control;

public interface  ViewResultsPhosphorus {
    
   public abstract  void addClientResult(
            String clientName,
            String requestSent,
            String jobSent,
            String resultReceive ,
            String requestNoSent,
            String relativeResultReceive, 
            String relativeRequestNoSent);

   public abstract void addResourceResult(
          String resourceName,
            String jobReceive,
            String jobSent,
            String relativeJobSent,
            String failSent,
            String relativeFailSent,
            String busyTime,
            String relativeBusyTime,
            String noAvailable,
            String relativeNoAvailable);       
             
           

   

   public abstract void addSwitchResult(
           final String switchName,
            final String jobSwitched,
            final String jobNoSwitched,
            final String resultSwiched,
            final String resultNoSwitched,
            final String requestSwitched,
            final String requestNoSwitched,
            final String relativeNojobNoSwitched,
            final String relativeResultNoSwitched,
            final String relativeRequestNoSwitched,
            final String relativeAllNoSwitched);

   public void setExecutionPercentage(double Percentage,double simulationTime);
}
