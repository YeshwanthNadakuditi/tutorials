package com.mule.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mule.DefaultMuleEvent;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.routing.AggregationContext;
import org.mule.routing.AggregationStrategy;

public class SortingTechnique2 implements AggregationStrategy {

	private  List<JSONObject> jsonValues = new ArrayList<JSONObject>();
	private  JSONObject jsonResp = new JSONObject();
	private JSONArray jsonArr = new JSONArray();
	private JSONObject jsonObj = new JSONObject();
	
	@Override
	public MuleEvent aggregate(AggregationContext context) throws MuleException {
		// TODO Auto-generated method stub
		MuleEvent result = null;
		for (MuleEvent event : context.collectEventsWithoutExceptions()) {
			String response = "";
			try {
				response = (String) event.getMessage().getPayloadAsString();
				jsonObj = new JSONObject(response);
				jsonArr = jsonObj.getJSONArray("records");
				for (int j = 0; j < jsonArr.length(); j++) {
					jsonValues.add(jsonArr.getJSONObject(j));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			if (result == null){
				result = DefaultMuleEvent.copy(event);
				result.getMessage().setPayload("");
			}
		}
		System.out.println(jsonValues);
		Collections.sort(jsonValues, new Comparator<JSONObject>() {
			private static final String KEY_NAME = "riCountryCode";
			@Override
			public int compare(JSONObject a, JSONObject b) {
				String valA = new String();
				String valB = new String();
				try {
					 valA = (String) a.get(KEY_NAME);
					 valB = (String) b.get(KEY_NAME);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return valA.compareTo(valB);
			}
		});
		if (result != null) {
			jsonResp.put("SortedRecords", jsonValues);
			result.getMessage().setPayload(jsonResp.toString());
			return result;
		}
		throw new RuntimeException("no response obtained");
	}
}
