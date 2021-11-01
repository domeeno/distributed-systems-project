package main

import (
	"fmt"
	"log"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
)

type userPricingDTO struct {
	userId         uuid.UUID `json:userId`
	planId         uuid.UUID `json:planId`
	previousPlanId uuid.UUID `json:previousPlanId`
	planExp        time.Time `json:planExp`
}

type userPricings []userPricingDTO

func getInfo(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "I am the pricing microservice")
}

func getPricing(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "get pricing by id")
}

func createPricing(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "get pricing by id")
}

func updatePricing(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "get pricing by id")
}

func handlePricing(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "Creating pricing")
}

func handleRequests() {

	router := mux.NewRouter().StrictSlash(true)

	router.HandleFunc("/info", getInfo)
	router.HandleFunc("/pricing/{id}", getPricing).Methods("GET")
	router.HandleFunc("/pricing/{id}", createPricing).Methods("POST")
	router.HandleFunc("/pricing/{id}", updatePricing).Methods("PUT")
	log.Fatal(http.ListenAndServe(":8081", router))
}

func main() {
	handleRequests()
}
