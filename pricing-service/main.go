package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"time"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/jinzhu/gorm"
	_ "github.com/jinzhu/gorm/dialects/postgres"
	"github.com/rs/cors"
)

var db *gorm.DB
var err error

type UserPricingDTO struct {
	userId         uuid.UUID `json:userId`
	planId         uuid.UUID `json:planId`
	previousPlanId uuid.UUID `json:previousPlanId`
	planExp        time.Time `json:planExp`
}

type Pricing struct {
	gorm.Model
	pricingId       uuid.UUID `gorm:"column:pricing_id;primaryKey"`
	userId          uuid.UUID `gorm:"column:user_id"`
	planId          uuid.UUID `gorm:"column:plan_id"`
	previousPlanId  uuid.UUID `gorm:"column:previous_plan_id"`
	planExp         time.Time `gorm:"column:plan_exp"`
	createTimestamp time.Time `gorm:"column:create_timestamp"`
}

func getInfo(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "I am the pricing microservice")
}

func getPricing(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	db.First(&pricing, params["pricingId"])
	json.NewEncoder(w).Encode(&pricing)
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

	mx := mux.NewRouter().StrictSlash(true)

	mx.HandleFunc("/info", getInfo)
	mx.HandleFunc("/pricing/{id}", getPricing).Methods("GET")
	mx.HandleFunc("/pricing/{id}", createPricing).Methods("POST")
	mx.HandleFunc("/pricing/{id}", updatePricing).Methods("PUT")

	router := cors.New(cors.Options{
		AllowedOrigins:   []string{""},
		AllowCredentials: true,
		// Enable Debugging for testing, consider disabling in production
		Debug: true,
	}).Handler(mx)

	log.Fatal(http.ListenAndServe(":8081", router))
}

func main() {
	db, err = gorm.Open("postgres", "host=localhost port=5432 user=postgres dbname=pandora sslmode=disable")
	if err != nil {

		panic("failed to connect database")

	}
	handleRequests()
}
