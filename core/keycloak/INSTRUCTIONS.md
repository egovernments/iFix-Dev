# Keycloak Setup

## Import Realm

1. Open the keycloak console
2. Near the top-left corner in the realm drop down menu, select **Add Realm**.
3. Select the [ifix-realm.json](./ifix-realm.json) file.

## Creating Clients

1. From the Clients section of Keycloak Admin Console, create a client. 
2. Provide unique username for the client. 
3. Go to client's settings
4. Change Access Type to **confidential**
5. Turn on **Service Account Enabled**
6. In the **Valid Redirect URIs** field provide the root url of the iFIX Instance. (Not important for our purposes but need to set it because it is mandatory)
7. And Save these changes
8. In the **Service Account Roles** tab, assign the role "fiscal-event-producer"
9. In the **Mappers** tab, create a new mapper to associate the client with a tenantId
	a. Select Mapper Type to be Hardcoded claim
	b. In Claim Name, write "tenantId"
	c. In Claim value, write the <government-id> under which the client is being created. (For example, "pb")
	d. Select Claim Json Type to be String

Now you can get the credentials from the **Credentials** tab and configure them in the client's system. 