package com.mule.message;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.DefaultMuleEvent;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.routing.AggregationContext;
import org.mule.routing.AggregationStrategy;

public class Sortingtechnique3 implements AggregationStrategy {
	private JSONObject jsonObj = new JSONObject();
	private JSONObject jsonResp = new JSONObject();
	private JSONArray jsonArr = new JSONArray();
	private  List<JSONObject> jsonValues = new ArrayList<JSONObject>();
	@Override
	public MuleEvent aggregate(AggregationContext context) throws MuleException {
		// TODO Auto-generated method stub
		MuleEvent result = null;
		for (MuleEvent event : context.collectEventsWithoutExceptions()) {
			String response = "";
			try {
				/*Concurrent Modification exception was due to parsing Mule event to JSON Obj*/
				response = (String) event.getMessage().getPayloadAsString();
				jsonObj = new JSONObject(response);
				jsonArr = jsonObj.getJSONArray("records");
				/* Using JSON Arrays cannot better the performance at all
				 * srcjsonArr =  jsonObj.getJSONArray("records");
				for (int i = 1; i < srcjsonArr.length(); i++) {
					destjsonArr.put(srcjsonArr.getJSONObject(i));
					//System.out.println(destjsonArr);
				}*/
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (int j = 0; j < jsonArr.length(); j++) {
				jsonValues.add(jsonArr.getJSONObject(j));
			}
			if (result == null){
			result = DefaultMuleEvent.copy(event);
			}
		}
		jsonResp.put("SortedRecords", jsonValues);
		result.getMessage().setPayload(jsonResp.toString());
		return result;
	}
}
