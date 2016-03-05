package in.ashwanthkumar.gocd.slack;


import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import in.ashwanthkumar.gocd.slack.util.TestUtils;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GoNotificationPluginTest {

    public static final String USER_HOME = "user.home";
    public static final String REQUEST_GET_CONFIGURATION = "go.plugin-settings.get-configuration";
    public static final String REQUEST_GET_VIEW = "go.plugin-settings.get-view";
    public static final String REQUEST_NOTIFICATIONS_INTERESTED_IN = "notifications-interested-in";
    public static final String REQUEST_VALIDATE_CONFIGURATION = "go.plugin-settings.validate-configuration";


    public static final String NOTIFICATION_INTEREST_RESPONSE = "{\"notifications\":[\"stage-status\"]}";
    public static final String GET_VIEW_RESPONSE = "{" +
            "\"template\":\"" +
            "<div class=\\\"form_item_block\\\">" +
            "<label>Message:" +
            "</label>" +
            "</div>\"" +
            "}";
    public static final String GET_CONFIGURATION_RESPONSE = "{\"server_url_external\":{" +
            "\"display-name\":\"External server URL\"" +
            "}" +
            "}";
    private static final String GET_CONFIG_VALIDATION_RESPONSE = "[]";


    @Test
    public void canHandleConfigValidationRequest() {

        GoNotificationPlugin plugin = createGoNotificationPlugin();

        GoPluginApiRequest request = mock(GoPluginApiRequest.class);
        when(request.requestName()).thenReturn(REQUEST_VALIDATE_CONFIGURATION);
        when(request.requestBody()).thenReturn("{\"plugin-settings\":" +
                "{\"external_server_url\":{\"value\":\"bob\"}}}");

        GoPluginApiResponse rv = plugin.handle(request);

        assertThat(rv, is(notNullValue()));
        assertThat(rv.responseBody(), equalTo(GET_CONFIG_VALIDATION_RESPONSE));

    }

    @Test
    public void canHandleConfigurationRequest() {

        GoNotificationPlugin plugin = createGoNotificationPlugin();

        GoPluginApiRequest request = mock(GoPluginApiRequest.class);
        when(request.requestName()).thenReturn(REQUEST_GET_CONFIGURATION);

        GoPluginApiResponse rv = plugin.handle(request);

        assertThat(rv, is(notNullValue()));
        assertThat(rv.responseBody(), equalTo(GET_CONFIGURATION_RESPONSE));

    }

    @Test
    public void canHandleGetViewRequest() {

        GoNotificationPlugin plugin = createGoNotificationPlugin();

        GoPluginApiRequest request = mock(GoPluginApiRequest.class);
        when(request.requestName()).thenReturn(REQUEST_GET_VIEW);

        GoPluginApiResponse rv = plugin.handle(request);

        assertThat(rv, is(notNullValue()));
        assertThat(rv.responseBody(), containsString("<div class=\\\""));

    }

    @Test
    public void canHandleNotificationInterestedInRequest() {

        GoNotificationPlugin plugin = createGoNotificationPlugin();

        GoPluginApiRequest request = mock(GoPluginApiRequest.class);
        when(request.requestName()).thenReturn(REQUEST_NOTIFICATIONS_INTERESTED_IN);

        GoPluginApiResponse rv = plugin.handle(request);

        assertThat(rv, is(notNullValue()));
        assertThat(rv.responseBody(), equalTo(NOTIFICATION_INTEREST_RESPONSE));

    }

    public static GoNotificationPlugin createGoNotificationPlugin() {
        String folder = TestUtils.getResourceDirectory("go_notify.conf");

        String oldUserHome = System.getProperty(USER_HOME);
        System.setProperty(USER_HOME, folder);

        GoNotificationPlugin plugin = new GoNotificationPlugin();

        System.setProperty(USER_HOME, oldUserHome);
        return plugin;
    }

}
