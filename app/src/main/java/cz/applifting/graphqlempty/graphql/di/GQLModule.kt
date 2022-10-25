package cz.applifting.graphqlempty.graphql.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import cz.applifting.graphqlempty.MainActivity
import cz.applifting.graphqlempty.MyApp
import cz.applifting.graphqlempty.graphql.login.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GQLModule {

    @Singleton
    @Provides
    @GQLHttpClient
    fun provideGQLOkHttpClient(@ApplicationContext context: Context):  OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(context))
            .build()
    }

    @Singleton
    @Provides
    fun provideApolloClient(@GQLHttpClient client: OkHttpClient): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
            .okHttpClient(client)
            .build()
    }

}

private class AuthorizationInterceptor(val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", User.getToken(context) ?: "")
            .build()

        return chain.proceed(request)
    }
}