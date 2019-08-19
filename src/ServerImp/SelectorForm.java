package ServerImp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Cascade.Initiator;
import Cascade.Input;
import Cascade.Results;
import Cascade.Vertex;

/**
 * Servlet implementation class SelectorForm
 */
@WebServlet("/SelectorForm")
public class SelectorForm extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectorForm() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\RAVINDER SINGH\\workspace\\FinalProject\\src\\Cascade\\inputFile"));
		String input = request.getParameter("input");
		System.out.println(input);
		Input inp = new Input();
		try{
			JSONObject inpJson = new JSONObject(input);
			if(!inpJson.getString("city").equals(""))
				inp.setCity(inpJson.getString("city"));
			if(!inpJson.getString("country").equals(""))
				inp.setCountry(inpJson.getString("country"));
			if(!inpJson.getString("age1").equals(""))
				inp.setAge1(Integer.parseInt(inpJson.getString("age1")));
			else
				inp.setAge1(0);
			if(!inpJson.getString("age2").equals(""))
				inp.setAge2(Integer.parseInt(inpJson.getString("age2")));
			else
				inp.setAge2(0);
			JSONArray brand = inpJson.getJSONArray("brand");
			for(int i=0;i<brand.length();i++){
				JSONObject temp = brand.getJSONObject(i);
				inp.setBrandMap(temp.getString("name"), Integer.parseInt(temp.getString("score")));
			}
			JSONArray person = inpJson.getJSONArray("personality");
			for(int i=0;i<person.length();i++){
				JSONObject temp = person.getJSONObject(i);
				inp.setPersonalityMap(temp.getString("name"), Integer.parseInt(temp.getString("score")));
			}
			JSONArray category = inpJson.getJSONArray("category");
			for(int i=0;i<category.length();i++){
				JSONObject temp = category.getJSONObject(i);
				inp.setCategoryMap(temp.getString("name"), Integer.parseInt(temp.getString("score")));
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		Initiator runPgm = new Initiator();
		runPgm.generateResult(inp);
		System.out.println("Done2");
		RequestDispatcher rd=request.getRequestDispatcher("/individualLayer.jsp");  
		rd.forward(request, response);
	}
}
