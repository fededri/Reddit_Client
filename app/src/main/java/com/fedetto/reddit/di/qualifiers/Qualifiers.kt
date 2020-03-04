package com.fedetto.reddit.di.qualifiers

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BasicAuthClient


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OAuthClient