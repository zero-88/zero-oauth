package com.zero.oauth.client.core.oauth2;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.zero.oauth.client.core.IPropertyModel;
import com.zero.oauth.client.type.FlowStep;
import com.zero.oauth.client.type.GrantType;

public class OAuth2ImplicitPropTest {

    private OAuth2RequestProperties requestProperties;
    private OAuth2ResponseProperties responseProperties;

    @Before
    public void init() {
        requestProperties = OAuth2RequestProperties.init(GrantType.IMPLICIT);
        responseProperties = OAuth2ResponseProperties.init(GrantType.IMPLICIT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestParams_FilterBy_Init() {
        requestProperties.by(FlowStep.INIT);
    }

    @Test
    public void testRequestParams_FilterBy_Authorize() {
        List<IPropertyModel> by = requestProperties.by(FlowStep.AUTHORIZE);
        List<String> param_names = by.stream().map(IPropertyModel::getName).collect(Collectors.toList());
        assertThat(param_names, hasItems("response_type", "client_id", "redirect_uri", "scope", "state"));
    }

    @Test
    public void testResponseParams_FilterBy_AccessToken() {
        List<IPropertyModel> by = responseProperties.by(FlowStep.AUTHORIZE);
        List<String> param_names = by.stream().map(IPropertyModel::getName).collect(Collectors.toList());
        assertThat(param_names, hasItems("access_token", "token_type", "expires_in", "scope"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequestParams_FilterBy_AccessToken() {
        requestProperties.by(FlowStep.ACCESS_TOKEN);
    }

    @Test
    public void testRequestParams_Implicit_Value() {
        OAuth2RequestProperties params = OAuth2RequestProperties.init(GrantType.IMPLICIT);
        OAuth2RequestProp customValue2 = params.getProp(OAuth2RequestProp.RESPONSE_TYPE.getName());
        assertNotSame(OAuth2RequestProp.RESPONSE_TYPE, customValue2);
        assertEquals("token", customValue2.getValue());
    }
}