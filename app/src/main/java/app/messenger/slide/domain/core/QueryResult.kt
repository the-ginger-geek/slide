package app.messenger.slide.domain.core

class QueryResult<R, E>() {
    private var result: R? = null
    private var exception: E? = null

    private constructor(r: R?, e: E?): this() {
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