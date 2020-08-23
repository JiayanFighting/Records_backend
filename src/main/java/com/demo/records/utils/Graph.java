package com.demo.records.utils;


import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.models.extensions.*;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.extensions.*;
import com.demo.records.aad.SimpleAuthProvider;
import com.demo.records.domain.UserDO;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Graph {

    private static IGraphServiceClient graphClient = null;
    private static SimpleAuthProvider authProvider = null;

    private static void ensureGraphClient(String accessToken) {
        // Create the auth provider
        authProvider = new SimpleAuthProvider(accessToken);

        // Create default logger to only log errors
        DefaultLogger logger = new DefaultLogger();
        logger.setLoggingLevel(LoggerLevel.ERROR);

        // Build a Graph client
        graphClient = GraphServiceClient.builder()
                .authenticationProvider(authProvider)
                .logger(logger)
                .buildClient();
    }

    public static User getUser(String accessToken) {
        ensureGraphClient(accessToken);

        // GET /me to get authenticated user
        User me = graphClient
                .me()
                .buildRequest()
                .get();

        return me;
    }

    public static void sendEmail(String accessToken,Message message ){
        ensureGraphClient(accessToken);

        try {
            graphClient.me()
                    .sendMail(message,null)
                    .buildRequest()
                    .post();
        }catch (Exception e){
            System.out.println("sendEmail : "+e.getMessage());
        }

    }

    public static void logoutGraphClient(){
        graphClient = null;
    }

    public static void getGroupMembers(String accessToken, String groupId){
        ensureGraphClient(accessToken);
        IDirectoryObjectCollectionWithReferencesPage members =
                graphClient.groups(groupId).members()
                .buildRequest()
                .get();


        ArrayList<UserDO> user = new ArrayList<>();

    }

    public static List<UserDO> searchUsers(String accessToken, String content) {
        ensureGraphClient(accessToken);

        final List<Option> options = new LinkedList<Option>();
        options.add(new QueryOption("search", content));
        IPersonCollectionPage people = graphClient.me().people()
                .buildRequest(options)
                .top(20)
                .select("displayName,scoredEmailAddresses")
                .get();

        List<UserDO> uList = new LinkedList<>();
        List<Person> pList = people.getCurrentPage();
        for (Person p : pList){
            String username = p.displayName;
            String email = p.scoredEmailAddresses.get(0).address;
            System.out.println("username = "+username+" , email = "+email);
            uList.add(new UserDO(email,username));
        }
        return uList;
    }

    public static void getPhoto(String accessToken) {
        ensureGraphClient(accessToken);

        // GET /me to get authenticated user
        InputStream stream = graphClient.me().photo().content()
                .buildRequest()
                .get();

//        System.out.println("phpto"+stream.toString());
//        return me;
    }
}
