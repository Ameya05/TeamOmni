
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
 
//import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
 
@Path("/")
public class StormClustering {
	@POST
	@Path("/clustering")
	@Consumes(MediaType.APPLICATION_XML) //MediaType Unconfirmed
	
	public Response sendCluster(InputStream incomingData) {
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				builder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + builder.toString());
 
		// return HTTP response 200 in case of success
		return Response.status(200).entity(builder.toString()).build(); //response as string
	}

 
}