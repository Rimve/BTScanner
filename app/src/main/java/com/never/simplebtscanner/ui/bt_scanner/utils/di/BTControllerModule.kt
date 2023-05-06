package com.never.simplebtscanner.ui.bt_scanner.utils.di

import android.content.Context
import com.never.simplebtscanner.ui.bt_scanner.utils.BTController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BTControllerModule {
    @Provides
    @Singleton
    fun provideBluetoothController(@ApplicationContext context: Context): BTController =
        BTController(context)
}
