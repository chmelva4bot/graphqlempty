package cz.applifting.graphqlempty.graphql.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import cz.applifting.graphqlempty.graphql.launchDetail.LaunchDetailViewModel
import cz.applifting.graphqlempty.graphql.launchList.LaunchListViewModel
import cz.applifting.graphqlempty.graphql.login.LoginViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val gqlModule = module {


    fun provideGQLOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(context))
            .build()
    }

    fun provideApolloClient(client: OkHttpClient): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://apollo-fullstack-tutorial.herokuapp.com/graphql")
            .webSocketServerUrl("wss://apollo-fullstack-tutorial.herokuapp.com/graphql")
            .okHttpClient(client)
            .build()
    }

    single { provideGQLOkHttpClient(get()) }
    single { provideApolloClient(get()) }

    viewModel { LaunchListViewModel(get()) }
    viewModel { LaunchDetailViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}