package app.messenger.slide.domain.core

class QueryResult<R, E>() {
    var value: R? = null
    var exception: E? = null

    private constructor(r: R?, e: E?): this() {
        value = r
        exception = e
    }

    fun isSuccessful(): Boolean = exception == null

    companion object {
        fun <R, E> failure(e: E): QueryResult<R, E> {
            return QueryResult(null, e)
        }

        fun <R, E> success(r: R): QueryResult<R, E> {
            return QueryResult(r, null)
        }
    }
}