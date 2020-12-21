package com.example.telainicio.classes

import android.graphics.Bitmap

class User {

    private var uid: String? = null
    private var status: String? = null
    private var nome: String? = null
    private var lowerNome: String? = null
    private var sobrenome: String? = null
    private var email: String? = null
    private var senha: String? = null
    private var apelido: String? = null
    private var dataNasc: String? = null
    private var areaTrab: String? = null
    private var orientacao: String? = null
    private var genero: Int = -1
    private var preferencia: Int = -1
    private var generoPref: Int = -1
    private var orientacaoPref: String? = null
    private var idadePref: String? = null
    private var fotoPerfil: String? = null
    private var foto1: String? = null
    private var foto2: String? = null
    private var foto3: String? = null
    private var bio: String? = null

    constructor()
    constructor(
        uid: String?,
        status: String?,
        nome: String?,
        lowerNome: String?,
        sobrenome: String?,
        email: String?,
        senha: String?,
        apelido: String?,
        dataNasc: String?,
        areaTrab: String?,
        orientacao: String?,
        genero: Int,
        preferencia: Int,
        generoPref: Int,
        orientacaoPref: String?,
        idadePref: String?,
        fotoPerfil: String?,
        foto1: String?,
        foto2: String?,
        foto3: String?,
        bio: String?
    ) {
        this.uid = uid
        this.status = status
        this.nome = nome
        this.lowerNome = lowerNome
        this.sobrenome = sobrenome
        this.email = email
        this.senha = senha
        this.apelido = apelido
        this.dataNasc = dataNasc
        this.areaTrab = areaTrab
        this.orientacao = orientacao
        this.genero = genero
        this.preferencia = preferencia
        this.generoPref = generoPref
        this.orientacaoPref = orientacaoPref
        this.idadePref = idadePref
        this.fotoPerfil = fotoPerfil
        this.foto1 = foto1
        this.foto2 = foto2
        this.foto3 = foto3
        this.bio = bio
    }

    fun getUid(): String?{
        return uid
    }

    fun setUid(uid: String){
        this.uid = uid
    }

    fun getStatus(): String?{
        return status
    }

    fun setStatus(status: String){
        this.status = status
    }

    fun getNome(): String?{
        return nome
    }

    fun setNome(nome: String){
        this.nome = nome
    }

    fun getLowerNome(): String?{
        return lowerNome
    }

    fun setLowerNome(lowerNome: String){
        this.lowerNome = lowerNome
    }

    fun getSobrenome(): String?{
        return sobrenome
    }

    fun setSobrenome(sobrenome: String){
        this.sobrenome = sobrenome
    }

    fun getEmail(): String?{
        return email
    }

    fun setEmail(email: String){
        this.email = email
    }

    fun getSenha(): String?{
        return senha
    }

    fun setSenha(senha: String){
        this.senha = senha
    }

    fun getApelido(): String?{
        return apelido
    }

    fun setApelido(apelido: String){
        this.apelido = apelido
    }

    fun getDataNasc(): String?{
        return dataNasc
    }

    fun setDataNasc(dataNasc: String){
        this.dataNasc = dataNasc
    }

    fun getAreaTrab(): String?{
        return areaTrab
    }

    fun setAreaTrab(areaTrab: String){
        this.areaTrab = areaTrab
    }

    fun getOrientacao(): String?{
        return orientacao
    }

    fun setOrientacao(orientacao: String){
        this.orientacao = orientacao
    }

    fun getGenero(): Int{
        return genero
    }

    fun setGenero(orientacao: Int){
        this.genero = genero
    }

    fun getPreferencia(): Int{
        return preferencia
    }

    fun setPreferencia(preferencia: Int){
        this.preferencia = preferencia
    }

    fun getGeneroPref(): Int{
        return generoPref
    }

    fun setGeneroPref(preferencia: Int){
        this.generoPref = generoPref
    }

    fun getOrientacaoPref(): String? {
        return orientacaoPref
    }

    fun setOrientacaoPref(preferencia: String){
        this.orientacaoPref = orientacaoPref
    }

    fun getIdadePref(): String? {
        return idadePref
    }

    fun setIdadePref(idade: String){
        this.idadePref = idade
    }

    fun getFotoPerfil(): String? {
        return fotoPerfil
    }

    fun setFotoPerfil(fotoPerfil: String){
        this.fotoPerfil = fotoPerfil
    }

    fun getFoto1(): String? {
        return foto1
    }

    fun setFoto1(foto1: String){
        this.foto1 = foto1
    }

    fun getFoto2(): String? {
        return foto2
    }

    fun setFoto2(foto2: String){
        this.foto2 = foto2
    }

    fun getFoto3(): String? {
        return foto3
    }

    fun setFoto3(foto3: String){
        this.foto3 = foto3
    }

    fun getBio(): String? {
        return bio
    }

    fun setBio(bio: String){
        this.bio = bio
    }

}