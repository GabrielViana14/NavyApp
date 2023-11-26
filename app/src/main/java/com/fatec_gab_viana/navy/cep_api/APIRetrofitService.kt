package com.fatec_gab_viana.navy.cep_api

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Path

interface APIRetrofitService {
    @GET("{CEP}/json/")
    fun getEnderecoByCEP(@Path("CEP") CEP : String) : Call<cep>

    @GET("{estado}/{cidade}/{endereco}/json/")
    fun getCEPByCidadeEstadoEndereco(@Path("estado") estado: String,
                                     @Path("cidade") cidade: String,
                                     @Path("endereco") endereco: String): Call<List<cep>>
}