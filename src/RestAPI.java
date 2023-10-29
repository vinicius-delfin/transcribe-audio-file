import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RestAPI  {
    public static void main(String[] args) throws Exception {
        Transcript transcript = new Transcript();
        String raw = "?raw=true";
        String url = "https://github.com/vinicius-delfin/audio_files/blob/master/tony-audio-scarface.mp3";
//        transcript.setAudio_url("https://github.com/johnmarty3/JavaAPITutorial/blob/main/Thirsty.mp4?raw=true");
        transcript.setAudio_url(url + raw);


        Gson gson = new Gson();
        String jsonRequest = gson.toJson(transcript);
//        System.out.println(jsonRequest);

        HttpRequest postResquest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript"))
                .header("Authorization", "46d88cbeb93a4bb9b3a45d5a67a998ea")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> postResponse = httpClient.send(postResquest, HttpResponse.BodyHandlers.ofString());
//        System.out.println(postResponse.body());
        transcript = gson.fromJson(postResponse.body(), Transcript.class);
        System.out.println(transcript.getId());

        HttpRequest getResquest = HttpRequest.newBuilder()
                .uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
                .header("Authorization", "46d88cbeb93a4bb9b3a45d5a67a998ea")
                .build();

        while (true) {
            HttpResponse<String> getResponse = httpClient.send(getResquest, HttpResponse.BodyHandlers.ofString());
            transcript = gson.fromJson(getResponse.body(), Transcript.class);
            System.out.println(transcript.getStatus());
            if ("completed".equals(transcript.getStatus()) || "error".equals(transcript.getStatus())) {
                break;
            }
            Thread.sleep(1000);
        }

        System.out.println("Transcription completed!");
        System.out.println(transcript.getText());
    }

}


