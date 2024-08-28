package com.example.fitnessapp.di

import com.example.fitnessapp.repository.ExerciseRepository
import com.example.fitnessapp.service.apiservice.ExerciseAPI
import com.example.fitnessapp.util.Constans.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokeApi() : ExerciseAPI
    {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExerciseAPI::class.java)
    }


    @Singleton
    @Provides
    fun provideExerciseRepository(
        api: ExerciseAPI
    ) = ExerciseRepository(api)


}