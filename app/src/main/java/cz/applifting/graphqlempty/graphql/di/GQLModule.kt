package cz.applifting.graphqlempty.graphql.di

import android.content.Context
import cz.applifting.graphqlempty.graphql.login.User
import okhttp3.Interceptor
import okhttp3.Response

//@Module
//@InstallIn(SingletonComponent::class)
//object GQLModule {
//
//    @Singleton
//    @Provides
//    @GQLHttpClient
//    fun provideGQLOkHttpClient(@ApplicationContext context: Context):  OkHttpClient{
//        return OkHttpClient.Builder()
//            .addInterceptor(AuthorizationInterceptor(context))
//            .build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideApolloClient(@GQLHttpClient client: OkHttpClient): ApolloClient {
//        return ApolloClient.Builder()
//            .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
//            .webSocketServerUrl("wss://apollo-fullstack-tutorial.herokuapp.com/graphql")
//            .okHttpClient(client)
//            .build()
//    }
//
//}

class AuthorizationInterceptor(val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", User.getToken(context) ?: "")
            .build()

        return chain.proceed(request)
    }
}