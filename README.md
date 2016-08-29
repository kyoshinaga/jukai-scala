# scala-jld
Workspace to implement a method to support JLD input for scala.

## Tokenizer@JukaiNLP
Tokenizer of [`JukaiNLP`](https://github.com/hshindo/JukaiNLP.jl.git). 

```julia
function Tokenizer()
    dict = IdDict(map(UTF8String, ["UNKNOWN"," ","\n"]))
    g = begin
        T = Float32
        local embed = Embedding(T,100,10)
        local conv = Conv(T, (10,7), (1,70), paddims=(0,3))
        local linear = Linear(T,70,4)
        @graph = (:chars,) begin
            x = Var(reshape(:chars,1,length(:chars)))
            x = embed(x)
            x = conv(x)
            x = reshape(x,size(x,2),size(x,3))
            x = transpose(x)
            x = relu(x)
            x = linear(x)
            x
         end
     end
    # model = conpile(g, :chars)
    Tokenizer(dict, IOE(), g)
end
```

