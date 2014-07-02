package edu.ag2.controller;

import Grid.Entity;
import edu.ag2.model.NetworkChecker;
import edu.ag2.model.SimulationBase;
import edu.ag2.presentation.NetworkErrorsReporter;
import edu.ag2.presentation.design.GraphNode;
import java.util.Iterator;
import java.util.Map;

public class ExecuteController extends ExecuteAbstractController {
   
    @Override
    public void initNetwork() {
        SimulationBase.getInstance().initNetwork();
    }

    @Override
    public void run() {

        Thread thread = new Thread(SimulationBase.getInstance());
        thread.start();
        
       //System.out.println("###########----  RUN ----####################################################");
    }

    @Override
    public void stop() {
        SimulationBase.getInstance().stop();
    }

    @Override
    public boolean isWellFormedNetwork() {
        
        //Hace las veces de modelo
        NetworkChecker networkChecker = new NetworkChecker();
        
        networkChecker.check();
        
        if(!networkChecker.passCheck()){
            
            NetworkErrorsReporter netErrorsReporter = new NetworkErrorsReporter();
            chargeErrorsListToShow(networkChecker, netErrorsReporter);
            netErrorsReporter.showReport();
            
            return false;
        }
        
        return true;
    }

    private void chargeErrorsListToShow(NetworkChecker networkChecker, NetworkErrorsReporter errorWindow) {
        
        Iterator itListOfErrors = networkChecker.getListOfErrors().entrySet().iterator();
        
        int errorCounter=1;
        while(itListOfErrors.hasNext()){
            
            Map.Entry<Object,String> errorInfo = (Map.Entry<Object,String>)itListOfErrors.next();
            
            if(errorInfo.getKey() instanceof Entity){
                GraphNode graphNode = findGraphNode(errorInfo.getKey());
                
                if(graphNode!= null){
                    errorWindow.addErrorToShow(errorCounter+". El nodo con nombre \""+graphNode.getName().toUpperCase()+"\": "+errorInfo.getValue());
                }
                
            }else{
                errorWindow.addErrorToShow(errorCounter+". LA SIMULACI\u00D3N:"+errorInfo.getValue());
            }
            
            errorCounter++;
        }
    }

    private GraphNode findGraphNode(Object object) {

        for(GraphNode graphNode:MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet()){
            
            if(MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(graphNode).equals(object)){
                return graphNode;
            }
        }

        return null;
    }

    @Override
    public void reLoadConfigFile() {
//       SimulationInstance.configuration.loadProperties();
      
    }
}